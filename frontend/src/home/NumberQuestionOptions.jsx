import {useEffect, useState} from 'react';
import PropTypes from "prop-types";
import {InputNumber} from "primereact/inputnumber";

const NumberQuestionOptions = ({responseOptions, setResponseOptions}) => {

    const updateResponseOptions = (e, boundToUpdate) => {
        setResponseOptions(prevState => {
            return {...prevState, [boundToUpdate]: e.value}
        })
    }

    return (
        <>
            <div className="formgrid grid">
                <div className="field col-12 md:col-6">
                    <label htmlFor="min" className="mb-1">Minimum value</label>
                    <InputNumber inputId="min" value={responseOptions["min"]} onChange={(e) =>
                        updateResponseOptions(e, "min")} showButtons className="w-auto"
                                 max={responseOptions["max"]}/>
                </div>
                <div className="field col-12 md:col-6">
                    <label htmlFor="max" className="mb-1">Maximum value</label>
                    <InputNumber inputId="max" value={responseOptions["max"]} onChange={(e) =>
                        updateResponseOptions(e, "max")} showButtons className="w-auto"
                                 min={responseOptions["min"]}/>
                </div>

            </div>

            </>
    )
}

NumberQuestionOptions.propTypes = {
    responseOptions: PropTypes.object.isRequired,
    setResponseOptions: PropTypes.func.isRequired
};

export default NumberQuestionOptions
