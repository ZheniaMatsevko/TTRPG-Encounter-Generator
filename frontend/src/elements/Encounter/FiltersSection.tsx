import { useContext } from "react";
import Filter from "./Filter.tsx";

export default function FiltersSection() {

    const {  } = useContext()

    return (
        <>
            {
                filters.map((filter) => (
                    <Filter
                        key={filter.field}
                        field={filter.field}
                        value={filter.value}
                        description={filter.description}
                    />
                ))
            }
        </>
    )

}