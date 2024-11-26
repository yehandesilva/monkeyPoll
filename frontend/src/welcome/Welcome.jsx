import {useContext, useRef, useState} from 'react';
import {Toast} from 'primereact/toast';
import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';
import {getSurvey} from "../api/surveyApi.js";
import {SurveyContext} from "../context/SurveyContext.jsx";

const Welcome = ({toast}) => {
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
        <>
            <Toast ref={toast}/>
            <div className="flex flex-column justify-content-center align-items-center gap-4 h-screen">
                <div className="flex justify-content-center">
                    <img src="monkeypoll-full-white.svg" alt="MonkeyPoll Logo"
                         style={{width: '50%', maxWidth: '1080px', height: 'auto'}}/>
                </div>
                <div className="flex pb-4">
                    <p className="text-2xl">Polls made simple.</p>
                </div>
                <div className="flex gap-2">
                    <InputText value={code} onChange={(e) => setCode(e.target.value)} placeholder="Enter Code"
                               className="p-inputtext-lg"/>
                    <Button icon="pi pi-arrow-right" size="large" style={{boxShadow: "none"}}
                            onClick={() => codeSubmit()}/>
                </div>
            </div>
        </>
    )
}

export default Welcome;