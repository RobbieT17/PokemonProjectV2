package project.services;

import project.game.pokemon.stats.Type;
import project.game.pokemon.stats.TypeName;

public class TypeSingleMatchups {

    public static void main(String[] args) {
        Type type = TypeMatchupHelpers.selectType(TypeName.values(), "Select a Type: ");
        System.out.printf("%nType: %s%n%nDefense:%n%s%nOffense:%n%s", 
            type.name().toUpperCase(), 
            TypeMatchupHelpers.showMatchups(TypeMatchupHelpers.calculateMatchups(type)),
            TypeMatchupHelpers.showMatchups(TypeMatchupHelpers.calculateMatchupsOffense(type))
        );
       
    }
}
