import {useEffect, useState} from 'react';
import { InputText } from 'primereact/inputtext';
import {Slider} from "primereact/slider";

const NumberQuestionOptions = () => {
    const [responseOptions, setResponseOptions] = useState([0,10])

    return (
        <>
            <div className="flex flex-row align-items-center">
                <div className="flex flex-column align-items-center">
                    <label htmlFor="min" className="mb-1">Min</label>
                    <InputText id="min" value={responseOptions[0]} onChange={(e) =>
                        setResponseOptions([e.target.value, responseOptions[1]])} className="w-5"/>
                </div>

                <Slider value={responseOptions} onChange={(e) =>
                    setResponseOptions(e.value)} range className="w-full"/>
                <div className="flex flex-column align-items-center">
                    <label htmlFor="max" className="mb-1">Max</label>
                    <InputText id="max" value={responseOptions[1]} onChange={(e) =>
                        setResponseOptions([responseOptions[0], e.target.value])} className="w-5" />
                </div>
            </div>


            </>
    )
}
export default NumberQuestionOptions
