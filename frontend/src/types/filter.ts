export type Filter = {
    type: FilterType
    value: FilterValue
    quantity?: number
}

export enum FilterType {
    MONSTER_NAME = 'monster_name',
    MONSTER_LOCATION = 'monster_location',
}

export type FilterValue = 'none' | 'any' | 'all';