import {Link} from "react-router-dom";

type HeaderProps = {
    path: string
    text: string
}

export default function TopMenu() {

    return (
        <div>
            <nav className='flex flex-auto flex-row justify-between align-middle
            w-full h-fit p-4 mb-4
            text-2xl
            border-b-2 backdrop-blur border-black
            '>
                <Logo path='/encounter' text='ENCOUNTER GENERATOR'/>
                <div className='flex flex-wrap justify-evenly w-1/2 h-fit'>
                    <Header path='/about' text='About'/>
                    <Header path='/custom-monster' text='Create custom monster'/>
                    <Header path='/settings' text='Settings'/>
                    <Header path='/credits' text='Credits'/>
                </div>
            </nav>
        </div>

    )

}

function Header({path, text}: HeaderProps) {
    return (
        <Link to={path}
              className='flex w-fit font-["EB_Garamond"] text-[#262626] transition delay-50 duration-300 ease-in-out h-fit hover:scale-110'>
            <h2>
                {text}
            </h2>
        </Link>
    )
}

function Logo({path, text}: HeaderProps) {
    return (
        <Link to={path} className='flex font-["Montserrat"] text-[#262626] font-extrabold h-fit'>
            <h2 className='text-[#262626]'>
                {text}
            </h2>
        </Link>
    )
}

