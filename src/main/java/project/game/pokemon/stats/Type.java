package project.game.pokemon.stats;

public enum Type {
        Bug(
            new TypeName[] { TypeName.Fighting, TypeName.Grass, TypeName.Ground, TypeName.Sound },
            new TypeName[] { TypeName.Fire, TypeName.Flying, TypeName.Rock }
        ),

        Dark(
            new TypeName[] { TypeName.Dark, TypeName.Digital, TypeName.Ghost },
            new TypeName[] { TypeName.Bug, TypeName.Fairy, TypeName.Fighting },
            new TypeName[] { TypeName.Psychic }
        ),

        Digital(
            new TypeName[] { TypeName.Electric, TypeName.Psychic, TypeName.Steel },
            new TypeName[] { TypeName.Bug, TypeName.Ice, TypeName.Poison },
            new TypeName[] { TypeName.Fairy }
        ),

        Dragon(
            new TypeName[] { TypeName.Electric, TypeName.Fire, TypeName.Grass, TypeName.Water },
            new TypeName[] { TypeName.Dragon, TypeName.Fairy, TypeName.Ice }
        ),

        Electric(
            new TypeName[] { TypeName.Electric, TypeName.Flying, TypeName.Steel },
            new TypeName[] { TypeName.Digital, TypeName.Ground }
        ),

        Fairy(
            new TypeName[] { TypeName.Bug, TypeName.Dark, TypeName.Fighting },
            new TypeName[] { TypeName.Digital, TypeName.Poison, TypeName.Sound, TypeName.Steel },
            new TypeName[] { TypeName.Dragon }
        ),

        Fighting(
            new TypeName[] { TypeName.Bug, TypeName.Dark, TypeName.Rock },
            new TypeName[] { TypeName.Fairy, TypeName.Flying, TypeName.Psychic, TypeName.Zombie }
        ),

        Fire(
            new TypeName[] { TypeName.Bug, TypeName.Fire, TypeName.Fairy, TypeName.Grass, TypeName.Ice, TypeName.Steel, TypeName.Zombie },
            new TypeName[] { TypeName.Ground, TypeName.Rock, TypeName.Water }
        ),

        Flying(
            new TypeName[] { TypeName.Bug, TypeName.Digital, TypeName.Fighting, TypeName.Grass },
            new TypeName[] { TypeName.Electric, TypeName.Ice, TypeName.Rock, TypeName.Space },
            new TypeName[] { TypeName.Ground }
        ),

        Ghost(
            new TypeName[] { TypeName.Bug, TypeName.Poison, TypeName.Zombie },
            new TypeName[] { TypeName.Dark, TypeName.Ghost },
            new TypeName[] { TypeName.Fighting, TypeName.Normal }
        ),

        Grass(
            new TypeName[] { TypeName.Electric, TypeName.Grass, TypeName.Ground, TypeName.Water },
            new TypeName[] { TypeName.Bug, TypeName.Fire, TypeName.Flying, TypeName.Ice, TypeName.Poison }
        ),

        Ground(
            new TypeName[] { TypeName.Digital, TypeName.Poison, TypeName.Rock, TypeName.Sound },
            new TypeName[] { TypeName.Grass, TypeName.Ice, TypeName.Water },
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
            new TypeName[] { TypeName.Bug, TypeName.Fairy, TypeName.Fighting, TypeName.Grass, TypeName.Poison },
            new TypeName[] { TypeName.Ground, TypeName.Psychic }
        ),

        Psychic(
            new TypeName[] { TypeName.Fighting, TypeName.Psychic, TypeName.Sound, TypeName.Space },
            new TypeName[] { TypeName.Bug, TypeName.Dark, TypeName.Ghost, TypeName.Zombie }
        ),

        Rock(
            new TypeName[] { TypeName.Fire, TypeName.Flying, TypeName.Normal, TypeName.Poison, TypeName.Space },
            new TypeName[] { TypeName.Fighting, TypeName.Grass, TypeName.Ground, TypeName.Steel, TypeName.Water }
        ),

        Sound(
            new TypeName[] { TypeName.Dark, TypeName.Fairy, TypeName.Sound, TypeName.Water },
            new TypeName[] { TypeName.Ground, TypeName.Space }
        ),

        Space(
            new TypeName[] { TypeName.Bug, TypeName.Fighting, TypeName.Grass, TypeName.Normal, TypeName.Space },
            new TypeName[] { TypeName.Ice, TypeName.Rock },
            new TypeName[] { TypeName.Flying, TypeName.Sound }
        ),

        Steel(
            new TypeName[] { TypeName.Bug, TypeName.Dragon, TypeName.Fairy, TypeName.Flying, TypeName.Grass, TypeName.Ice,
                            TypeName.Normal, TypeName.Psychic, TypeName.Rock, TypeName.Space, TypeName.Steel, TypeName.Zombie },
            new TypeName[] { TypeName.Fire, TypeName.Fighting, TypeName.Ground, TypeName.Digital },
            new TypeName[] { TypeName.Poison }
        ),

        Water(
            new TypeName[] { TypeName.Fire, TypeName.Ice, TypeName.Steel, TypeName.Water },
            new TypeName[] { TypeName.Electric, TypeName.Grass, TypeName.Sound }
        ),

        Zombie(
            new TypeName[] { TypeName.Bug, TypeName.Dark, TypeName.Electric, TypeName.Fighting, TypeName.Ghost,
                            TypeName.Ground, TypeName.Ice, TypeName.Normal, TypeName.Poison, TypeName.Psychic },
            new TypeName[] { TypeName.Fire, TypeName.Grass, TypeName.Steel },
            new TypeName[] { TypeName.Zombie }
        );

        private final TypeName[] resistances;
        private final TypeName[] weaknesses;
        private final TypeName[] immunities;

        Type(TypeName[] resist, TypeName[] weak) {
            this.resistances = resist;
            this.weaknesses = weak;
            this.immunities = new TypeName[] {};
        }

        Type(TypeName[] resist, TypeName[] weak, TypeName[] immune) {
            this.resistances = resist;
            this.weaknesses = weak;
            this.immunities = immune;
        }

        public TypeName toTypeName() { return TypeName.valueOf(this.name()); }
        public TypeName[] getResistances() { return this.resistances; }
        public TypeName[] getWeaknesses() { return this.weaknesses; }
        public TypeName[] getImmunities() { return this.immunities; }
    }