package com.quackers.plugin.utils;

import java.util.*;

public class eGirlUtil {
    private static final Random random = new Random();

    private static final String[] faces = {
        "(・`ω´・)", ";;w;;", "OwO", "UwU", ">w<", "^w^", "ÚwÚ", "^-^", ":3", "x3"
    };

    private static final String[] expressions = {
        "nyaa~~", "mya", "rawr", "rawr x3", "rawr XD", "XD", "meow", "meow~"
    };

    private static final String[] actions = {
        "*blushes*", "*whispers to self*", "*cries*", "*screams*", "*sweats*",
        "*twerks*", "*runs away*", "*screeches*", "*walks away*", "*sees bulge*",
        "*looks at you*", "*notices bulge*", "*starts twerking*", "*huggles tightly*", "*boops your nose*"
    };

    private static final String[] suffixes = {
        " uwu~", " >w<", " rawr~", " ✨", " ~nya", " x3", " 🥺", " 💞", " :3"
    };

    private static final String[] emojis = {
        "🥺", "✨", "💖", "💞", "😳", "😳👉👈", "🌸", "💕", "(*^‿^*)", "♡"
    };

    private static final Map<String, String> wordMap = Map.ofEntries(
        Map.entry("you", "yuu"),
        Map.entry("you're", "ur"),
        Map.entry("love", "wuv"),
        Map.entry("cute", "kawaii~"),
        Map.entry("hello", "hewwo"),
        Map.entry("hi", "haii"),
        Map.entry("no", "nuu"),
        Map.entry("stop", "stap"),
        Map.entry("yes", "yush")
    );

    public static String uwuify(String message, boolean useFaces, boolean useExpressions, boolean useActions,
                                boolean useStutter, boolean useSuffix, boolean useEmojis) {
        String[] words = message.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            String original = word.toLowerCase();
            String uwuWord = wordMap.getOrDefault(original, word);

            uwuWord = uwuWord.replaceAll("[rl]", "w").replaceAll("[RL]", "W");

            if (useStutter && randomChance(0.10) && uwuWord.length() > 2) {
                uwuWord = uwuWord.charAt(0) + "-" + uwuWord.charAt(0) + "-" + uwuWord;
            }

            if (useFaces && randomChance(0.075)) uwuWord += " " + randomFrom(faces);
            else if (useExpressions && randomChance(0.075)) uwuWord += " " + randomFrom(expressions);
            else if (useActions && randomChance(0.075)) uwuWord += " " + randomFrom(actions);

            result.append(uwuWord).append(" ");
        }

        String output = result.toString().trim();

        if (useSuffix && randomChance(0.5)) output += randomFrom(suffixes);
        if (useEmojis && randomChance(0.3)) output += " " + randomFrom(emojis);

        return output.replaceAll(" +", " ");
    }

    private static boolean randomChance(double chance) {
        return random.nextDouble() < chance;
    }

    private static String randomFrom(String[] array) {
        return array[random.nextInt(array.length)];
    }
}
