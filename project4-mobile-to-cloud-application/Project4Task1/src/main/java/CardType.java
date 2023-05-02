/**
 * andrewId: qinlinj
 * author: Justin Jia
 */

public enum CardType {
    HERO("Hero"),
    MINION("Minion"),
    SPELL("Spell"),
    ENCHANTMENT("Enchantment"),
    WEAPON("Weapon"),
    HERO_POWER("Hero Power");

    private final String name;

    CardType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
