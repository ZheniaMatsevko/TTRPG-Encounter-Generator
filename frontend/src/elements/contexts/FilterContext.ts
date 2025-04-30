import React, {createContext} from "react";
import {State} from "../../types/state.ts";

const FilterContext = createContext<{
    state?: State,
    setState?:  React.Dispatch<React.SetStateAction<State>>
}>({});

export default FilterContext;