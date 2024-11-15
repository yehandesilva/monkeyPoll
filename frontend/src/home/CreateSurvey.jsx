import { useState, useRef, useContext, useEffect } from 'react';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { getSurvey } from "../api/surveyApi.js";
import { SurveyContext } from "../context/SurveyContext.jsx";
import { UserContext } from "../context/UserContext.jsx";
import Home from '../home/Home.jsx';
import {logoutUser, registerUser} from "../api/userApi.js";
import {Accordion, AccordionTab} from "primereact/accordion";
import {Dropdown} from "primereact/dropdown";

const CreateSurvey = () => {
    const toast = useRef(null);
    const [user] = useContext(UserContext);
    const [showHome, setShowHome] = useState(false);


    // useEffect(() => {
    //     setShowHome(!!(user || survey));
    // }, [user, survey]);


    if (showHome) {
        return <Home />;
    }

    const onSubmit = async () => {
        //const createStatus = await createSurvey(questionList);
    };

    return (
        <>
            <Button label="Back to Home" icon="pi pi-home" size="small" className="absolute top-0 left-0 m-4"
                    style={{boxShadow: "none"}} onClick={() => setShowHome(true)}/>
            <div className="flex flex-column align-items-center gap-3 p-4 card">
                <Button label="Create Survey!" icon="pi pi-send" size="small"
                        style={{boxShadow: "none"}} onClick={onSubmit}/>
            </div>
            </>
    );
};

export default CreateSurvey;