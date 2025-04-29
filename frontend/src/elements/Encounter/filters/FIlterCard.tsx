import {QuantityRequirement} from "../../../dto/filterDto.ts";


export function FilterCard({ value, field, quantity }: { value: string, field: string, quantity: QuantityRequirement }) {


    return (
        <div className="w-full h-24 flex place-content-between flex-wrap mt-4 p-2 border-1 rounded-md">
            <h3 className="w-full  text-3xl italic">{field}  —  {String(value)}</h3>
            <h4 className="font-extralight text-xl">Quantity: {quantity}</h4>
        </div>
    )

}