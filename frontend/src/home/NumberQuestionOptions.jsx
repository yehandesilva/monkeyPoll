import {useEffect, useState} from 'react';
import PropTypes from "prop-types";
import {InputNumber} from "primereact/inputnumber";

const NumberQuestionOptions = ({responseOptions, setResponseOptions}) => {


    const updateMinOption = (e) => {
        if (!responseOptions["max"] || e.value < responseOptions["max"]) {
            // either there is no max, or the new value is lower than max, so set min to this value
            setResponseOptions(prevState => {
                return {...prevState, min: e.value}
            })
        } else {
            // new value is invalid, so set it to the current max

            setResponseOptions(prevState => {
                return {...prevState, min: responseOptions["max"]}
            })
        }
    }

    const updateMaxOption = (e) => {
        if (!responseOptions["min"] || e.value > responseOptions["min"]) {
            // either there is no min, or the new value is higher than min, so set max to this value
            setResponseOptions(prevState => {
                return {...prevState, max: e.value}
            })
        } else {
            // new value is invalid, so set it to the current max
            setResponseOptions(prevState => {
                return {...prevState, max: responseOptions["min"] }
            })
        }
    }

    return (
        <>
            <div className="formgrid grid">
                <div className="field col-12 md:col-6">
                    <label htmlFor="min" className="mb-1">Minimum value</label>
                    <InputNumber inputId="min" value={responseOptions["min"]} onChange={(e) =>
                        updateMinOption(e)} showButtons className="w-auto"
                                 max={responseOptions["max"]}/>
                </div>
                <div className="field col-12 md:col-6">
                    <label htmlFor="max" className="mb-1">Maximum value</label>
                    <InputNumber inputId="max" value={responseOptions["max"]} onChange={(e) =>
                        updateMaxOption(e)} showButtons className="w-auto"
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
