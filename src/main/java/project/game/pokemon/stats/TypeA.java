package project.game.pokemon.stats;

public enum TypeA { 
    Bug(
        new TypeName[] { TypeName.Digital, TypeName.Fighting, TypeName.Grass, TypeName.Ground, TypeName.Sound},
        new TypeName[] { TypeName.Fire, TypeName.Flying, TypeName.Rock }
    ),

    Dark(
        new TypeName[] { TypeName.Dark, TypeName.Digital, TypeName.Ghost, TypeName.Space },
        new TypeName[] { TypeName.Bug, TypeName.Fairy, TypeName.Fighting },
        new TypeName[] { TypeName.Psychic }
    ),

    Digital(
        new TypeName[] { TypeName.Dragon, TypeName.Electric, TypeName.Fairy, TypeName.Psychic, TypeName.Space, TypeName.Steel },
        new TypeName[] { TypeName.Bug, TypeName.Dark, TypeName.Grass, TypeName.Ice, TypeName.Poison }
    ),

    Dragon(
        new TypeName[] { TypeName.Electric, TypeName.Fire, TypeName.Grass, TypeName.Water },
        new TypeName[] { TypeName.Dragon, TypeName.Fairy, TypeName.Ice }
    ),

    Electric(
        new TypeName[] { TypeName.Electric, TypeName.Flying, TypeName.Space, TypeName.Steel },
        new TypeName[] { TypeName.Digital, TypeName.Ground }
    ),

    Fairy(
        new TypeName[] { TypeName.Bug, TypeName.Dark, TypeName.Fighting, TypeName.Sound},
        new TypeName[] { TypeName.Digital, TypeName.Poison, TypeName.Steel },
        new TypeName[] { TypeName.Dragon }
    ),

    Fighting(
        new TypeName[] { TypeName.Bug, TypeName.Dark, TypeName.Digital, TypeName.Rock, TypeName.Sound},
        new TypeName[] { TypeName.Fairy, TypeName.Flying, TypeName.Psychic, TypeName.Space, TypeName.Zombie }
    ),

    Fire(
        new TypeName[] { TypeName.Bug, TypeName.Fire, TypeName.Fairy, TypeName.Grass, TypeName.Ice, TypeName.Steel, TypeName.Zombie },
        new TypeName[] { TypeName.Ground, TypeName.Rock, TypeName.Water }
    ),

    Flying(
        new TypeName[] { TypeName.Bug, TypeName.Digital, TypeName.Fighting, TypeName.Grass, TypeName.Sound, TypeName.Zombie },
        new TypeName[] { TypeName.Electric, TypeName.Ice, TypeName.Rock, TypeName.Space },
        new TypeName[] { TypeName.Ground }
    ),

    Ghost(
        new TypeName[] { TypeName.Bug, TypeName.Poison, TypeName.Zombie },
        new TypeName[] { TypeName.Dark, TypeName.Ghost },
        new TypeName[] { TypeName.Fighting, TypeName.Normal }
    ),

    Grass(
        new TypeName[] { TypeName.Electric, TypeName.Grass, TypeName.Ground, TypeName.Water, TypeName.Zombie },
        new TypeName[] { TypeName.Bug, TypeName.Fire, TypeName.Flying, TypeName.Ice, TypeName.Poison, TypeName.Space }
    ),

    Ground(
        new TypeName[] { TypeName.Digital, TypeName.Poison, TypeName.Rock},
        new TypeName[] { TypeName.Grass, TypeName.Ice, TypeName.Sound, TypeName.Water },
        new TypeName[] { TypeName.Electric }
    ),

    Ice(
        new TypeName[] { TypeName.Ice, TypeName.Space },
        new TypeName[] { TypeName.Fighting, TypeName.Fire, TypeName.Rock, TypeName.Sound, TypeName.Steel }
    ),

    Normal(
        new TypeName[] {},
        new TypeName[] { TypeName.Fighting, TypeName.Zombie },
        new TypeName[] { TypeName.Ghost }
    ),

    Poison(
        new TypeName[] { TypeName.Bug, TypeName.Digital, TypeName.Fairy, TypeName.Fighting, TypeName.Grass, TypeName.Poison },
        new TypeName[] { TypeName.Ground, TypeName.Psychic }
    ),

    Psychic(
        new TypeName[] { TypeName.Fighting, TypeName.Psychic, TypeName.Sound, TypeName.Space },
        new TypeName[] { TypeName.Bug, TypeName.Dark, TypeName.Ghost, TypeName.Zombie }
    ),

    Rock(
        new TypeName[] { TypeName.Fire, TypeName.Flying, TypeName.Normal, TypeName.Poison, TypeName.Space },
        new TypeName[] { TypeName.Fighting, TypeName.Grass, TypeName.Ground, TypeName.Sound, TypeName.Steel, TypeName.Water }
    ),

    Sound(
        new TypeName[] { TypeName.Dark, TypeName.Ghost, TypeName.Poison, TypeName.Sound},
        new TypeName[] { TypeName.Digital, TypeName.Psychic, TypeName.Space }
    ),

    Space(
        new TypeName[] { TypeName.Bug, TypeName.Fighting, TypeName.Fire, TypeName.Grass, TypeName.Space, TypeName.Water},
        new TypeName[] { TypeName.Ice, TypeName.Psychic, TypeName.Rock },
        new TypeName[] { TypeName.Flying, TypeName.Sound }
    ), 

    Steel(
        new TypeName[] { TypeName.Bug, TypeName.Dragon, TypeName.Fairy, TypeName.Flying, TypeName.Grass, TypeName.Ice,
                        TypeName.Normal, TypeName.Psychic, TypeName.Rock, TypeName.Space, TypeName.Steel, TypeName.Zombie },
        new TypeName[] { TypeName.Digital, TypeName.Fire, TypeName.Fighting, TypeName.Ground, TypeName.Sound },
        new TypeName[] { TypeName.Poison }
    ),

    Water(
        new TypeName[] { TypeName.Fire, TypeName.Ice, TypeName.Steel, TypeName.Water },
        new TypeName[] { TypeName.Electric, TypeName.Grass, TypeName.Sound }
    ),

    Zombie(
        new TypeName[] { TypeName.Bug, TypeName.Dark, TypeName.Fighting, TypeName.Ghost, TypeName.Ground, 
                        TypeName.Normal, TypeName.Poison, TypeName.Psychic},
        new TypeName[] { TypeName.Fire, TypeName.Grass, TypeName.Steel },
        new TypeName[] { TypeName.Zombie }
    );

    private final TypeName[] high;
    private final TypeName[] low;
    private final TypeName[] none;

    TypeA(TypeName[] resist, TypeName[] weak) {
        this.high = resist;
        this.low = weak;
        this.none = new TypeName[] {};
    }

    TypeA(TypeName[] high, TypeName[] low, TypeName[] none) {
        this.high = high;
        this.low = low;
        this.none = none;
    }

    public TypeName toTypeName() { return TypeName.valueOf(this.name()); }
    public TypeName[] getHighs() { return this.high; }
    public TypeName[] getLows() { return this.low; }
    public TypeName[] getNones() { return this.none; }
}
