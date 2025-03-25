

export default function Filter({field, value, quantity}: FilterProps) {

    return (
        <div className="w-full m-2 p-2 border-1 rounded-xl">
            <span>{field}</span>
            <span>{value}</span>
            <span>{quantity}</span>
            {/*<span className="hidden">{description}</span>*/}
        </div>
    )
}

export type FilterProps = {
    field: string
    value: string
    // description: string
    quantity: number
}