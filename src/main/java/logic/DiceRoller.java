package logic;

public class DiceRoller {
    public enum Attack {
        Hit, Critical, Focus, Miss
    }

    public enum Defense {
        Evade, Focus, Miss
    }

    public static Attack RollAttack() {
        int roll = (int)(Math.random() * 8);
        if (roll < 3)
            return Attack.Hit;
        else if (roll < 4)
            return Attack.Critical;
        else if (roll < 6)
            return Attack.Focus;
        else
            return Attack.Miss;
    }

    public static Defense RollDefense() {
        int roll = (int)(Math.random() * 8);
        if (roll < 3)
            return Defense.Evade;
        else if (roll < 5)
            return Defense.Focus;
        else
            return Defense.Miss;
    }
}
