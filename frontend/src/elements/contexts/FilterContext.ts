import {createContext} from "react";
import {Filter} from "../../types/filter.ts";

const FilterContext = createContext<{
    filters?: Filter[],
    setFilters?:  React.Dispatch<React.SetStateAction<Filter[]>>
}>({});

export default FilterContext;