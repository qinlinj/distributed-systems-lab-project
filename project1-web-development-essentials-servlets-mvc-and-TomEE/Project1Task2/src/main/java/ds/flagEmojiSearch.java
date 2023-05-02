package ds;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class flagEmojiSearch {
    // list to store the flag emoji data
    private static List<FlagEmoji> flagEmojis = new ArrayList<>();

    public static String searchFlagEmoji(String countryName) {
        try {
            URL url = new URL("https://cdn.jsdelivr.net/npm/country-flag-emoji-json@2.0.0/dist/index.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            Gson gson = new Gson();
            Type flagEmojiListType = new TypeToken<ArrayList<FlagEmoji>>(){}.getType();
            flagEmojis = gson.fromJson(reader, flagEmojiListType);

            String imageURL = "";
            for (FlagEmoji flagEmoji : flagEmojis) {
                if (flagEmoji.getName().equalsIgnoreCase(countryName)) {
                    imageURL = flagEmoji.getImage();
                    break;
                }
            }

            if (imageURL.isEmpty()) {
                return "Flag emoji image not found for the country: " + countryName;
            } else {
                return imageURL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while searching for flag emoji";
        }
    }
    // class to represent a flag emoji
    private static class FlagEmoji {
        private String name;
        private String code;
        private String emoji;
        private String unicode;
        private String image;

        public FlagEmoji(String name, String code, String emoji, String unicode, String image) {
            this.name = name;
            this.code = code;
            this.emoji = emoji;
            this.unicode = unicode;
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getEmoji() {
            return emoji;
        }

        public void setEmoji(String emoji) {
            this.emoji = emoji;
        }

        public String getUnicode() {
            return unicode;
        }

        public void setUnicode(String unicode) {
            this.unicode = unicode;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}