import {useEffect, useState} from "react";
import {FilterParam, QuantityRequirement} from "../../../dto/filterDto.ts";
import {State} from "../../../types/state.ts";
import {FilterOptions} from "../../../types/filter.ts";


const FilterForm = (
    {state, setState}: {
    state?: State,
    setState?: React.Dispatch<React.SetStateAction<State>>
    }) => {

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
        console.log(propertyName);
        if (state && state.filterOptions[propertyName as keyof FilterOptions]) {
            console.log(Object.keys(state.filterOptions))
            // console.log(state)
            setAvailableValues(
                state.filterOptions[propertyName as keyof FilterOptions].map(x => String(x.description)).sort()
            );
            setValue(state.filterOptions[propertyName as keyof FilterOptions].map(x => String(x.description))[0]);
        }
    }, [propertyName, state]);

    useEffect(() => {
        if (state && setState) {
            state.generationRequest.filters = filterParams;
            setState(state)
        }
    }, [filterParams]);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const newFilterParam: FilterParam = {
            propertyName,
            value,
            quantity: {
                quantityRequirement,
                ...(number && { number: parseInt(number) })
            }
        };


        setFilterParams([...filterParams, newFilterParam]);
        if (setState){
            state?.generationRequest?.filters?.push(newFilterParam);
            if (state) setState(state);
        }

        setQuantityRequirement(QuantityRequirement.NONE);
        setNumber('');
    };

    return (
        <div className="p-4">
            <h2 className="text-xl font-bold mb-4">Filter Parameters</h2>

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
                        <label className="block mb-1">Quantity Requirement</label>
                        <select
                            value={number}
                            onChange={(e) => setNumber(e.target.value)}
                            className="w-full p-2 border rounded"
                        >
                            {Object.values(QuantityRequirement).map((req) => (
                                <option key={req} value={req}>
                                    {req}
                                </option>
                            ))}
                        </select>
                    </div>

                </div>

                <button
                    type="submit"
                    className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                    Add Filter
                </button>
            </form>
        </div>
    );
};

export default FilterForm;