import { useState, useRef, useContext, useEffect } from 'react';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { getSurvey } from "../api/surveyApi.js";
import { SurveyContext } from "../context/SurveyContext.jsx";
import { UserContext } from "../context/UserContext.jsx";
import Home from '../home/Home.jsx';
import Survey from "../survey/Survey.jsx";

const Welcome = () => {
    const toast = useRef(null);
    const [survey, setSurvey] = useContext(SurveyContext);
    const [user] = useContext(UserContext);
    const [code, setCode] = useState((window.location.pathname).toString().substring(1));
    const [showHome, setShowHome] = useState(false);
    const [showSurvey, setShowSurvey] = useState(false);

    useEffect(() => {
        setShowHome(!!user);
    }, [user]);

    // Only direct user to the Survey's page if the survey
    // has been set (valid survey returned from backend)
    useEffect( () => {
        setShowSurvey(!!survey);
    }, [survey]);

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

    if (showHome) {
        return <Home />;
    }
    // Return the Survey page if valid survey returned
    // from the backend, so the user can fill it out and
    // submit their response
    if (showSurvey) {
        return <Survey />;
    }

    return (
        <>
            <Toast ref={toast} />
            <div className="flex flex-column justify-content-center align-items-center gap-4 h-full">
                <div className="flex justify-content-center">
                    <img src="monkeypoll-full-white.svg" alt="MonkeyPoll Logo"
                         style={{ width: '50%', maxWidth: '1080px', height: 'auto' }} />
                </div>
                <div className="flex pb-4">
                    <p style={{ fontSize: '1.4rem' }}>Polls made simple.</p>
                </div>
                <div className="flex gap-2">
                    <InputText value={code} onChange={(e) => setCode(e.target.value)} placeholder="Enter Code"
                               className="p-inputtext-lg" />
                    <Button icon="pi pi-arrow-right" size="large" style={{ boxShadow: "none" }} onClick={() => codeSubmit()} />
                </div>
            </div>
        </>
    )
}

export default Welcome;