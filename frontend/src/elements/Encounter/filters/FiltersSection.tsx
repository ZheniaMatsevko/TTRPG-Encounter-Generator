import {useContext, useEffect, useState} from "react";
import FilterContext from "../../contexts/FilterContext.ts";
import {FilterParam, QuantityRequirement} from "../../../dto/filterDto.ts";
import {FilterOptions} from "../../../types/filter.ts";
import {FilterCard} from "./FIlterCard.tsx";

export default function FiltersSection() {

    const { state, setState } = useContext(FilterContext)

    const [filterParams, setFilterParams] = useState<FilterParam[]>([]);


    const [availableProperties, setAvailableProperties] = useState<string[]>(['property']);
    const [propertyName, setPropertyName] = useState<string>('property');

    const [availableValues, setAvailableValues] = useState<string[]>(['value']);
    const [value, setValue] = useState<string>('value');

    const [quantityRequirement, setQuantityRequirement] = useState<QuantityRequirement>(QuantityRequirement.NONE);
    const [number, setNumber] = useState<string>('');

    // effect to set available properties
    useEffect(() => {
        if (state) {
            setAvailableProperties(Object.keys(state.filterOptions));
            setPropertyName(Object.keys(state.filterOptions)[0] ?? '');
        }
    }, [setAvailableProperties, state, state?.filterOptions]);

    // effect to set available values
    useEffect(() => {
        if (state && state.filterOptions[propertyName as keyof FilterOptions]) {
            setAvailableValues(
                state.filterOptions[propertyName as keyof FilterOptions].map(x => String(x.description)).sort()
            );
            setValue(state.filterOptions[propertyName as keyof FilterOptions].map(x => String(x.description))[0]);
        }
    }, [propertyName, setAvailableValues, state, state?.filterOptions]);

    useEffect(() => {
        if (state && setState) {
            state.generationRequest.filters = filterParams;
            setState(state)
        }
    }, [filterParams]);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        let propertyValue: string | boolean = value;
        if (propertyValue === 'false' || propertyValue === 'true') {
            propertyValue = propertyValue === 'true'
        }

        const newFilterParam: FilterParam = {
            propertyName,
            value: propertyValue,
            quantity: {
                quantityRequirement,
                ...(number && { number: parseInt(number) })
            }
        };

        if (state?.generationRequest.filters?.find(x => {
            return x.propertyName === propertyName &&
                x.value === newFilterParam.value &&
                x.quantity.quantityRequirement !== newFilterParam.quantity.quantityRequirement;
        })) {
            alert("Filters conflict")
            return;
        }

        if (state?.generationRequest.filters?.find(x => {
            return x.propertyName === propertyName &&
                x.value === newFilterParam.value &&
                x.quantity.quantityRequirement === newFilterParam.quantity.quantityRequirement;
        })) {
            return;
        }


        setFilterParams([...filterParams, newFilterParam]);
        if (setState){
            state?.generationRequest?.filters?.push(newFilterParam);
            if (state) setState(state);
        }

        console.log(state);

        setQuantityRequirement(QuantityRequirement.NONE);
        setNumber('');
    };

    function removeFilters(e: React.FormEvent) {
        e.preventDefault();

        setFilterParams([]);
        if (state && setState) {
            state.generationRequest.filters = [];
            setState(state);
        }
    }


    return (
        <div className="p-4">
            <h1 className="text-3xl font-bold mb-4">Filters</h1>

            <form onSubmit={handleSubmit} className="mb-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block mb-1">Property Name</label>
                        <select
                            value={propertyName}
                            onChange={(e) => setPropertyName(e.target.value)}
                            className="w-full p-2 border rounded"
                            required
                        >
                            {availableProperties.map((option) => (
                                <option key={option} value={option}>
                                    {option}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label className="block mb-1">Value</label>
                        <select
                            value={value}
                            onChange={(e) => setValue(e.target.value)}
                            className="w-full p-2 border rounded"
                            required
                        >
                            {availableValues.map((option) => (
                                <option key={option} value={option}>
                                    {option}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label className="block mb-1">Quantity Requirement</label>
                        <select
                            value={quantityRequirement}
                            onChange={(e) => setQuantityRequirement(e.target.value as QuantityRequirement)}
                            className="w-full p-2 border rounded"
                        >
                            {Object.values(QuantityRequirement).map((req) => (
                                <option key={req} value={req}>
                                    {req}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className={quantityRequirement === QuantityRequirement.EXACT ? "" : "hidden"}>
                        <label className="block mb-1">Number of monsters</label>
                        <input
                            type="number"
                            value={number}
                            onChange={(e) => setNumber(e.target.value)}
                            className="w-full p-2 border rounded"
                            required={quantityRequirement === QuantityRequirement.EXACT}
                        >
                            {/*{Object.values(QuantityRequirement).map((req) => (*/}
                            {/*    <option key={req} value={req}>*/}
                            {/*        {req}*/}
                            {/*    </option>*/}
                            {/*))}*/}
                        </input>
                    </div>

                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <button
                        type="submit"
                        className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition duration-300 ease-in-out"
                    >
                        Add Filter
                    </button>

                    <button
                        type="button"
                        className="mt-4 px-4 py-2 bg-red-500 text-white rounded hover:bg-red-700 transition duration-300 ease-in-out"
                        onClick={removeFilters}
                    >
                        Clear Filters
                    </button>
                </div>

            </form>

            <hr/>

            <div>
                <ul>
                    {filterParams.map((filterParam, index) => (
                        <FilterCard
                            field={filterParam.propertyName}
                            value={filterParam.value}
                            quantity={filterParam.quantity.quantityRequirement}
                            exact={filterParam.quantity.number}
                            key={index}
                        />
                    ))}
                </ul>
            </div>


        </div>
    );

}