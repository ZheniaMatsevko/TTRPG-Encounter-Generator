import {QuantityRequirement} from "../../../dto/filterDto.ts";


export function FilterCard({ value, field, quantity, exact }: { value: string, field: string, quantity: QuantityRequirement, exact?: number }) {


    return (
        <div className="w-full h-24 flex place-content-between flex-wrap mt-4 p-2 border-1 rounded-md">
            <h3 className="w-full  text-3xl italic">{field}  —  {String(value)}</h3>
            <h4 className="font-extralight text-xl">Quantity: {exact ? exact : quantity}</h4>
        </div>
    )

}