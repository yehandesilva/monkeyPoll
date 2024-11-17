import {useEffect, useRef, useState} from 'react';
import { InputText } from 'primereact/inputtext';
import PropTypes from "prop-types";
import {Knob} from "primereact/knob";
import {Button} from "primereact/button";
import Question from "./Question.jsx";

const ChoiceQuestionOptions = ({responseOptions, setResponseOptions}) => {
    const [numOptions, setNumOptions] = useState(1)
    const [optionList, setOptionList] = useState([
        <div className="flex flex-column m-2" key="1">
            <label htmlFor={"option-1"} className="mb-1">Option 1</label>
            <InputText id={"option-1"} placeholder="Enter response option" onChange={(e) =>
                updateResponseOptions(e)} className="w-auto"/>
        </div>
    ])
    const MAX_OPTIONS = 10
    const MIN_OPTIONS = 1

    const updateResponseOptions = (e) => {
        setResponseOptions(prevState => {
            return {...prevState, [e.target.id]: e.target.value}
        })
    }

    const handleNumOptionsChanged = (newVal) => {
        if (newVal > numOptions && newVal <= MAX_OPTIONS) {
            // Adding an option
            const newOption =
                <div className="flex flex-column m-2 " key={newVal}>
                    <label htmlFor={"option-" + newVal} className="mb-1">Option {newVal}</label>
                    <InputText id={"option-"+newVal} placeholder="Enter response option" onChange={(e) =>
                        updateResponseOptions(e)} className="w-auto"/>
                </div>

            setOptionList(prevState => {
                return [...prevState, newOption]
            })

        } else if (newVal < numOptions && newVal >= MIN_OPTIONS) {
            // Removing an option
            const copyArr = [...optionList];
            copyArr.pop();
            setOptionList(copyArr);
        }

        setNumOptions(newVal)
    }

    return (
        <>
            <div className="flex flex-row align-items-center">
                <label htmlFor="optionKnob" className="mr-3">Number of Options:</label>
                <div className="flex flex-row align-items-center gap-2">
                    <Button icon="pi pi-minus" onClick={() => handleNumOptionsChanged(numOptions - 1)} disabled={numOptions === 0} />
                    <Knob id="optionKnob" value={numOptions} size={70} min={MIN_OPTIONS} max={MAX_OPTIONS} readOnly/>
                    <Button icon="pi pi-plus" onClick={() => handleNumOptionsChanged(numOptions + 1)} disabled={numOptions === MAX_OPTIONS} />
                </div>
            </div>
            {optionList}
        </>
    )
}

ChoiceQuestionOptions.propTypes = {
    responseOptions: PropTypes.object.isRequired,
    setResponseOptions: PropTypes.func.isRequired
};
export default ChoiceQuestionOptions
