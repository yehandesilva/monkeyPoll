import {InputText} from "primereact/inputtext";
import {Button} from "primereact/button";
import {useContext, useState} from "react";
import {SurveyContext} from "../context/SurveyContext.jsx";
import {getSurvey} from "../api/surveyApi.js";

const SurveyCode = ({toast, large}) => {

    const [survey, setSurvey] = useContext(SurveyContext);
    const [code, setCode] = useState((window.location.pathname).toString().substring(1));

    const codeSubmit = async () => {
        if (code) {
            const surveyStatus = await getSurvey(code);
            if (surveyStatus.success) {
                setSurvey(surveyStatus.body);
            } else {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Survey Error',
                    detail: surveyStatus.body.message,
                });
            }
        } else {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Survey Error',
                detail: 'Survey code is required',
            });
        }
    }

    return (
        <div className="flex">
            <InputText value={code} onChange={(e) => setCode(e.target.value)} placeholder="Enter Code"
                       className={(large) ? "p-inputtext-lg" : ""}/>
            <Button icon="pi pi-arrow-right" size={(large) ? "large" : "small"} style={{boxShadow: "none"}}
                    onClick={() => codeSubmit()}/>
        </div>
    )
}

export default SurveyCode;
