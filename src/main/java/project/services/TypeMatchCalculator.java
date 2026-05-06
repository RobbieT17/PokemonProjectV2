package project.services;

import project.game.pokemon.stats.Type;
import project.game.pokemon.stats.TypeName;

public class TypeMatchCalculator {

    public static void main(String[] args) {
        Type type1 = TypeMatchupHelpers.selectType(TypeName.values(), "Select First Type: ");
        Type type2 = TypeMatchupHelpers.selectType(TypeMatchupHelpers.removeElement(Type.values(), type1), "Select Second Type: ");
            
        System.out.printf("%nType: %s-%s%n%n", type1.name().toUpperCase(), type2.name().toUpperCase());
        TypeMatchupHelpers.showMatchups(TypeMatchupHelpers.calculateMatchups(type1, type2));
    }
}
