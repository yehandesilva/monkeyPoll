import {useEffect, useState} from 'react';
import PropTypes from "prop-types";
import Question from "./Question.jsx";
import {InputNumber} from "primereact/inputnumber";

const NumberQuestionOptions = ({responseOptions, setResponseOptions}) => {
    return (
        <>
            <div className="formgrid grid">
                <div className="field col-12 md:col-6">
                    <label htmlFor="min" className="mb-1">Minimum value</label>
                    <InputNumber inputId="min" value={responseOptions[0]} onChange={(e) =>
                        setResponseOptions([e.value, responseOptions[1]])} showButtons className="w-auto"
                                 max={responseOptions[1]}/>
                </div>
                <div className="field col-12 md:col-6">
                    <label htmlFor="max" className="mb-1">Maximum value</label>
                    <InputNumber inputId="max" value={responseOptions[1]} onChange={(e) =>
                        setResponseOptions([responseOptions[0], e.value])} showButtons className="w-auto"
                                 min={responseOptions[0]}/>
                </div>

            </div>

            </>
    )
}

NumberQuestionOptions.propTypes = {
    responseOptions: PropTypes.array.isRequired,
    setResponseOptions: PropTypes.func.isRequired
};

export default NumberQuestionOptions
