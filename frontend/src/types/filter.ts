export type Filter = {
    type: FilterType
    value: FilterValue
    quantity?: number
}

export enum FilterType {
    MONSTER_NAME = 'monster_name',
    MONSTER_LOCATION = 'monster_location',
}

export type FilterOptions = {
    Tactics: FilterDto[];
    Habitats: FilterDto[];
    Activities: FilterDto[];
    Spellcaster: FilterDto[];
    Lair: FilterDto[];
    Legendary: FilterDto[];
    Alignment: FilterDto[];
    Tag: FilterDto[];
    Size: FilterDto[];
    Type: FilterDto[]
}

export type FilterDto = {
    id?: any;
    description: any;
}

export type FilterValue = 'none' | 'any' | 'all' | 'exact';