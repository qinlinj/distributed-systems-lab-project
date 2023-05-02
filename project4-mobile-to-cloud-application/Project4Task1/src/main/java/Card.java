/**
 * andrewId: qinlinj
 * author: Justin Jia
 */

public class Card {
    protected String name;
    protected String cardId;
    protected String cardClass;
    protected int cardCost;
    protected String cardFlavor;
    protected String cardRarity;
    protected String cardImageUrl;
    protected boolean collectible;
    protected CardType type;

    public Card(String name, String cardId, String cardClass, int cardCost, String cardFlavor, String cardRarity, String cardImageUrl) {
        this.name = name;
        this.cardId = cardId;
        this.cardClass = cardClass;
        this.cardCost = cardCost;
        this.cardFlavor = cardFlavor;
        this.cardRarity = cardRarity;
        this.cardImageUrl = cardImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getCardId() {
        return cardId;
    }

    public String getCardClass() {
        return cardClass;
    }

    public int getCardCost() {
        return cardCost;
    }

    public String getCardFlavor() {
        return cardFlavor;
    }

    public String getCardRarity() {
        return cardRarity;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public boolean isCollectible() {
        return collectible;
    }

    public CardType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                ", cardClass='" + cardClass + '\'' +
                ", cardCost=" + cardCost +
                ", cardFlavor='" + cardFlavor + '\'' +
                ", cardRarity='" + cardRarity + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                ", collectible=" + collectible +
                ", type=" + type +
                '}';
    }
}

class HeroCard extends Card {
    private int armor;

    public HeroCard(String name, String cardId, String cardClass, int cardCost, String cardFlavor, String cardRarity, String cardImageUrl, int armor) {
        super(name, cardId, cardClass, cardCost, cardFlavor, cardRarity, cardImageUrl);
        this.armor = armor;
        this.collectible = true;
        this.type = CardType.HERO;
    }

    public int getArmor() {
        return armor;
    }

    @Override
    public String toString() {
        return "HeroCard{" +
                "name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                ", cardClass='" + cardClass + '\'' +
                ", cardCost=" + cardCost +
                ", cardFlavor='" + cardFlavor + '\'' +
                ", cardRarity='" + cardRarity + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                ", collectible=" + collectible +
                ", type=" + type +
                ", armor=" + armor +
                '}';
    }
}

class MinionCard extends Card {
    private String race;
    private int attack;
    private int health;

    public MinionCard(String name, String cardId, String cardClass, int cardCost, String cardFlavor, String cardRarity, String cardImageUrl, String race, int attack, int health) {
        super(name, cardId, cardClass, cardCost, cardFlavor, cardRarity, cardImageUrl);
        this.race = race;
        this.attack = attack;
        this.health = health;
        this.collectible = true;
        this.type = CardType.MINION;
    }

    public String getRace() {
        return race;
    }

    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {
        return "MinionCard{" +
                "name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                ", cardClass='" + cardClass + '\'' +
                ", cardCost=" + cardCost +
                ", cardFlavor='" + cardFlavor + '\'' +
                ", cardRarity='" + cardRarity + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                ", race='" + race + '\'' +
                ", attack=" + attack +
                ", health=" + health +
                ", collectible=" + collectible +
                ", type=" + type +
                '}';
    }
}

class SpellCard extends Card {
    private String text;

    public SpellCard(String name, String cardId, String cardClass, int cardCost, String cardFlavor, String cardRarity, String cardImageUrl, String text) {
        super(name, cardId, cardClass, cardCost, cardFlavor, cardRarity, cardImageUrl);
        this.text = text;
        this.collectible = true;
        this.type = CardType.SPELL;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "SpellCard{" +
                "name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                ", cardClass='" + cardClass + '\'' +
                ", cardCost=" + cardCost +
                ", cardFlavor='" + cardFlavor + '\'' +
                ", cardRarity='" + cardRarity + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                ", text='" + text + '\'' +
                ", collectible=" + collectible +
                ", type=" + type +
                '}';
    }
}

class WeaponCard extends Card {
    private int attack;
    private int durability;

    public WeaponCard(String name, String cardId, String cardClass, int cardCost, String cardFlavor, String cardRarity, String cardImageUrl, int attack, int durability) {
        super(name, cardId, cardClass, cardCost, cardFlavor, cardRarity, cardImageUrl);
        this.attack = attack;
        this.durability = durability;
        this.collectible = true;
        this.type = CardType.WEAPON;
    }

    public int getAttack() {
        return attack;
    }

    public int getDurability() {
        return durability;
    }

    @Override
    public String toString() {
        return "WeaponCard{" +
                "name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                ", cardClass='" + cardClass + '\'' +
                ", cardCost=" + cardCost +
                ", cardFlavor='" + cardFlavor + '\'' +
                ", cardRarity='" + cardRarity + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                ", attack=" + attack +
                ", durability=" + durability +
                ", collectible=" + collectible +
                ", type=" + type +
                '}';
    }
}





