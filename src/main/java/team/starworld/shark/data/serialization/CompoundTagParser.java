package team.starworld.shark.data.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.*;

public class CompoundTagParser {

    public static String stringify (CompoundTag tag) {
        var map = tag.saveAsMap();
        var stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (var entry : map.entrySet()) {
            stringBuilder.append(stringify(entry.getKey()))
                .append(":")
                .append(stringify(entry.getValue()))
                .append(",");
        }
        if (stringBuilder.toString().endsWith(",")) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * 将数据文本化
     * @see CompoundTagParser
     */
    @SneakyThrows
    public static String stringify (String string) {
        return new ObjectMapper().writeValueAsString(string);
    }

    public static String stringify (ListTag listTag) {
        var list = listTag.saveAsList();
        var stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (var i = 0; i < list.size(); i ++) {
            var value = list.get(i);
            stringBuilder.append(new BigDecimal(i).toBigInteger()).append(":").append(stringify(value)).append(",");
        }
        if (stringBuilder.toString().endsWith(",")) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @SneakyThrows
    public static String stringify (Object object) {
        if (object instanceof CompoundTag tag) return stringify(tag);
        else if (object instanceof Map <?, ?> map) return stringify(new CompoundTag().load(map));
        else if (object instanceof Long value) return new BigDecimal(value) + "L";
        else if (object instanceof Double value) return new BigDecimal(value) + "D";
        else if (object instanceof Short value) return new BigDecimal(value) + "S";
        else if (object instanceof Integer value) return new BigDecimal(value) + "I";
        else if (object instanceof Float value) return new BigDecimal(value) + "F";
        else if (object instanceof Boolean value) return (value ? 1 : 0) + "B";
        else if (object instanceof String string) return stringify(string);
        else if (object instanceof Iterable <?> iterable) {
          var list = new ListTag();
          iterable.forEach(list::add);
          return stringify(list);
        } else {
            var mapper = new ObjectMapper();
            return stringify(new CompoundTag().load(mapper.readValue(mapper.writeValueAsString(object), new TypeReference <Map <?, ?>> () {})));
        }
    }

    public enum TokenType {
        String, Boolean, Integer, Double, Float, Short, Long, Null,
        TagStart, TagEnd, ListStart, ListEnd, Colon, Comma;

        public boolean isValue () {
            return switch (this) {
                case String, Boolean, Integer, Double, Float, Short, Long, Null -> true;
                default -> false;
            };
        }

    }

    @RequiredArgsConstructor
    public static class Token {

        @Getter
        private final TokenType tokenType;

        @Getter
        private final Object value;

        @Getter
        private final String raw;

        @Override @SneakyThrows
        public String toString () {
            var mapper = new ObjectMapper();
            return "Token (%s) [%s] %s".formatted(tokenType, mapper.writeValueAsString(raw), mapper.writeValueAsString(value));
        }

    }

    public static class Tokenizer {

        public static final String WHITESPACE_CHARACTER = "\n\r \t";
        public static final String RESERVED_CHARACTER = "{}[]:,";

        @Getter
        private int position = -1;

        @Getter
        private final String text;

        public Tokenizer (String text) {
            this.text = text;
        }

        public void reset () {
            position = 0;
        }

        public boolean next () {
            if (text.length() <= position) return false;
            position ++;
            return true;
        }

        public IllegalArgumentException makeError (String message) {
            return new IllegalArgumentException("[%s] %s".formatted(position, message));
        }

        public List <Token> tokenize () {
            var tokens = new ArrayList <Token> ();
            while (next()) {
                var character = character();
                if (character == null) break;
                if (RESERVED_CHARACTER.contains(character.toString())) {
                    tokens.add(
                        new Token(
                            switch (character) {
                                case '[' -> TokenType.ListStart;
                                case ']' -> TokenType.ListEnd;
                                case '{' -> TokenType.TagStart;
                                case '}' -> TokenType.TagEnd;
                                case ':' -> TokenType.Colon;
                                case ',' -> TokenType.Comma;
                                default -> throw makeError("Invalid character '%s'".formatted(character));
                            },
                            character.toString(), character.toString()
                        )
                    );
                    continue;
                }
                if (WHITESPACE_CHARACTER.contains(character.toString())) continue;
                if (character == '"') tokens.add(buildString());
                else if (character() == 't' && character(1) == 'r' && character(2) == 'u' && character(3) == 'e') {
                    next();
                    next();
                    next();
                    tokens.add(new Token(TokenType.Boolean, true, "true"));
                } else if (character() == 'n' && character(1) == 'u' && character(2) == 'l' && character(3) == 'l') {
                    next();
                    next();
                    next();
                    tokens.add(new Token(TokenType.Null, null, "null"));
                } else if (character() == 'f' && character(1) == 'a' && character(2) == 'l' && character(3) == 's' && character(4) == 'e') {
                    next();
                    next();
                    next();
                    next();
                    tokens.add(new Token(TokenType.Boolean, false, "false"));
                } else if (character().toString().matches("[0-9]")) {
                    tokens.add(buildNumber());
                } else {
                    var string = new StringBuilder();
                    do {
                        var stringCharacter = character();
                        if (stringCharacter == null) break;
                        if (WHITESPACE_CHARACTER.contains(character().toString()) || character() == '\n' || character() == '\r')
                            break;
                        if (":,[]{}".contains(stringCharacter.toString())) {
                            back();
                            break;
                        }
                        string.append(stringCharacter);
                    } while (next());
                    tokens.add(new Token(TokenType.String, string.toString(), string.toString()));
                }
            }
            return tokens;
        }

        public Token buildNumber () {
            var number = new StringBuilder();
            number.append(character());
            while (next()) {
                if (character() == null) break;
                if (character() == '.' && number.toString().contains(".")) throw makeError("Invalid character '.'");
                if (character().toString().matches("[0-9]") || character() == '.')
                    number.append(character());
                else {
                    back();
                    break;
                }
            }
            next();
            var type = character();
            var hasType = "IiFfDdLlSsBb".contains(type.toString());
            if (!hasType) {
                type = 'd';
                back();
            }
            var doubleValue = Double.parseDouble(number.toString());
            var raw = hasType ? number.toString() + type : number.toString();
            return switch (type) {
                case 'b', 'B' -> new Token(TokenType.Boolean, doubleValue != 0, raw);
                case 'i', 'I' -> new Token(TokenType.Integer, (int) doubleValue, raw);
                case 's', 'S' -> new Token(TokenType.Short, (short) doubleValue, raw);
                case 'l', 'L' -> new Token(TokenType.Long, (long) doubleValue, raw);
                case 'f', 'F' -> new Token(TokenType.Float, (float) doubleValue, raw);
                case 'd', 'D' -> new Token(TokenType.Double, doubleValue, raw);
                default -> throw makeError("Invalid type: " + type);
            };
        }

        @SneakyThrows
        public Token buildString () {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(character());
            next();
            while (character() != null && (character() != '"' || text.charAt(position - 1) == '\\')) {
                if (character() == '\\') {
                    stringBuilder.append(character());
                    next();
                    stringBuilder.append(character());
                } else {
                    stringBuilder.append(this.character());
                }
                next();
            }
            stringBuilder.append(character());
            if (!stringBuilder.toString().endsWith("\"")) throw makeError("Invalid or unexpected token");
            return new Token(TokenType.String, new ObjectMapper().readValue(stringBuilder.toString(), new TypeReference <> () {}), stringBuilder.toString());
        }

        public Character character () {
            try {
                return text.charAt(position);
            } catch (Throwable ignored) {
                return null;
            }
        }

        public Character character (int offset) {
            try {
                return text.charAt(position + offset);
            } catch (Throwable ignored) {
                return null;
            }
        }

        public boolean back () {
            if (position > 0) {
                position --;
                return true;
            }
            return false;
        }

    }

    /**
     * 解析数据标签<br/><br/>
     * 格式: <code>{compoundTag: { booleanTrue: 1b, booleanFalse: 0b, string: "HelloWorld", simpleString: string }, listTag: [0: 1b], integer: 1I, long: 1L, float: 1F, double: 1D, short: 1S, booleanTrue: true, booleanFalse: false}</code><br/><br/>
     * 数字标识（B=Boolean, F=Float, D=Double, L=Long, S=Short, I=Integer）可忽略大小写，如 <code>1b</code> = <code>1B</doe3>
     * @param text 源文本
     * @return 解析后的数据标签
     */
    public static CompoundTag parse (String text) {
        return new CompoundTagParser(new Tokenizer(text).tokenize()).parse();
    }

    @Getter
    private final List <Token> tokens = new ArrayList <> ();

    @Getter
    private int position = -1;

    public CompoundTagParser (Iterable <Token> iterable) {
        iterable.forEach(tokens::add);
    }

    public IllegalArgumentException makeError (String message) {
        throw new IllegalArgumentException("[%s] %s".formatted(position, message));
    }

    public CompoundTag parse () {
        if (tokens.get(0) == null || tokens.get(tokens.size() - 1) == null)
            throw makeError("Invalid first and last tokens");
        if (tokens.get(0).getTokenType() != TokenType.TagStart || tokens.get(tokens.size() - 1).getTokenType() != TokenType.TagEnd)
            throw makeError("Invalid first and last tokens");
        return parseTag();
    }

    public CompoundTag parseTag () {
        var tag = new CompoundTag();
        next();
        while (next()) {
            var current = current();
            if (current == null) throw makeError("Expect token '}'");
            if (current.getTokenType() == TokenType.TagEnd) break;
            if (current(1) == null) throw makeError("Invalid token %s".formatted(current.toString()));
            var value = current(2);
            if (current.getTokenType() == TokenType.Comma) continue;
            if (current.getTokenType() == TokenType.String && current(1).getTokenType() == TokenType.Colon) {
                next();
                tag.put(String.valueOf(current.getValue()), parseExpression());
                var next = current(1);
                if (next.getTokenType().isValue())
                    throw makeError("Invalid token %s, perhaps you forgot a comma?".formatted(next.toString()));
            } else throw makeError("Invalid token %s".formatted(current.toString()));
        }
        return tag;
    }

    public ListTag parseList () {
        var list = new ArrayList <Map.Entry <Number, Object>> ();
        next();
        while (next()) {
            var current = current();
            if (current == null) throw makeError("Expect token ']'");
            if (current.getTokenType() == TokenType.ListEnd) break;
            if (current(1) == null) throw makeError("Invalid token %s".formatted(current.toString()));
            var value = current(2);
            if (current.getTokenType() == TokenType.Comma) continue;
            if (current.getTokenType().isValue() && current(1).getTokenType() == TokenType.Colon) {
                next();
                list.add(Map.entry ((Number) current.getValue(), parseExpression()));
                var next = current(1);
                if (next.getTokenType().isValue())
                    throw makeError("Invalid token %s, perhaps you forgot a comma?".formatted(next.toString()));
            } else throw makeError("Invalid token %s".formatted(current.toString()));
        }
        var emptyObject = new Object();
        var parsedList = new ListTag();
        for (var i = 0; i < list.size(); i ++) parsedList.add(emptyObject);
        list.forEach(value -> parsedList.set(value.getKey().intValue(), value.getValue()));
        parsedList.removeIf(i -> i == emptyObject);
        return parsedList;
    }

    public Object parseExpression () {
        if (current(1).getTokenType() == TokenType.TagStart) return parseTag();
        else if (current(1).getTokenType() == TokenType.ListStart) return parseList();
        else if (current(1).getTokenType().isValue()) {
            next();
            return current().getValue();
        }
        throw makeError("Invalid token %s".formatted(current(1)));
    }

    public void reset () {
        position = 0;
    }

    public boolean next () {
        if (tokens.size() <= position) return false;
        position ++;
        return true;
    }

    public Token current () {
        try {
            return tokens.get(position);
        } catch (Throwable ignored) {
            return null;
        }
    }

    public Token current (int offset) {
        try {
            return tokens.get(position + offset);
        } catch (Throwable ignored) {
            return null;
        }
    }

}
