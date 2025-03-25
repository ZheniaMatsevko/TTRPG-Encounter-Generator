import { useContext } from "react";
import Filter from "./Filter.tsx";
import FilterContext from "../contexts/FilterContext.ts";

export default function FiltersSection() {

    const { filters } = useContext(FilterContext)

    return (
        <>
            {
                filters?.map((filter, i) => (
                    <Filter
                        key={i}
                        field={filter.type}
                        value={filter.value}
                        quantity={filter.quantity ? filter.quantity : 0}
                    />
                ))
            }
        </>
    )

}