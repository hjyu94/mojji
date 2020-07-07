package me.hjeong.mojji.formatter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class KeywordsFormatter implements Formatter<List<String>> {
    @Override
    public List<String> parse(String s, Locale locale) throws ParseException {
        String[] keywordArr = s.split("[,\\s]+");
        return Arrays.asList(keywordArr);
    }

    @Override
    public String print(List<String> strings, Locale locale) {
        return strings.stream().reduce((a,b) -> a + ", " + b).orElseThrow();
    }
}
