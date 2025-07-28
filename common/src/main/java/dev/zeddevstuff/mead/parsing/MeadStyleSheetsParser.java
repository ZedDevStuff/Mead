package dev.zeddevstuff.mead.parsing;

import java.util.Optional;
import java.util.regex.Pattern;

public class MeadStyleSheetsParser
{
    private final static Pattern COMMENT_PATTERN = Pattern.compile("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/", Pattern.DOTALL);
    private final static Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
    private final static Pattern RULE_PATTERN = Pattern.compile("(?<rule>\\.?\\w+\\s*\\{(?:[\\r\\n]|.)*})", Pattern.DOTALL);
    public Optional<Object> parse(String styleSheet)
    {
        var cleanedStyleSheet = removeComments(styleSheet);
        cleanedStyleSheet = flatten(cleanedStyleSheet);
        var rules = extractRules(cleanedStyleSheet);
        
        return Optional.empty();
    }
    private String removeComments(String input)
    {
        if (input == null || input.isEmpty())
        {
            return input;
        }
        return COMMENT_PATTERN.matcher(input).replaceAll("");
    }
    // Remove all newlines but preserve whitespace
    private String flatten(String input)
    {
        if (input == null || input.isEmpty())
        {
            return input;
        }
        return input.replaceAll("\r", "");
    }
    private String[] extractRules(String styleSheet)
    {
        if (styleSheet == null || styleSheet.isEmpty())
        {
            return new String[0];
        }
        return RULE_PATTERN.matcher(styleSheet).results()
                .map(matchResult -> matchResult.group("rule").trim())
                .toArray(String[]::new);
    }
}
