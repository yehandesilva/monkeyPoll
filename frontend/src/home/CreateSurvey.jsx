import { useState, useRef, useContext, useEffect } from 'react';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { UserContext } from "../context/UserContext.jsx";
import Home from '../home/Home.jsx';
import Question from "./Question.jsx";
import {createSurvey} from "../api/surveyApi.js";

const CreateSurvey = () => {
    const toast = useRef(null);
    const lastQuestionId = useRef(1)
    const [user] = useContext(UserContext);
    const [showHome, setShowHome] = useState(false);
    const [questionContents, setQuestionContents] = useState({})
    const [questionList, setQuestionList] = useState([<Question key={1} id={1} setQuestionContents={setQuestionContents}/>])


    if (showHome) {
        return <Home />;
    }

    const getUniqueId = () =>  {
        return ++lastQuestionId.current;
    }

    const onSubmit = async () => {
        /*
        TODO: validate inputs and adjust behaviour once creation endpoint is up.
        right now we don't know what will be returned from the backend, so we'll likely want to add some
        pop up to display the survey code, or return some info about the survey to populate the user's homepage
        */
        console.log(questionContents) //TODO: remove log

        const creationStatus = await createSurvey(questionContents);
        if (creationStatus.success) {
            toast.current.show({
                severity: 'success',
                life: 3000,
                summary: 'Survey successfully created!',
                detail: creationStatus.body.message,
            });
            setShowHome(true) //TODO: returning to home, but may need to change
        } else {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Creation Error',
                detail: creationStatus.body.message,
            });
        }
    };

    const handleAddQuestion = () => {
        const newId = getUniqueId()

        setQuestionList(prevState => {
            return [...prevState, <Question key={newId} id={newId} setQuestionContents={setQuestionContents}/>]
        })
    };

    return (
        <>
            <Toast ref={toast}/>
            <Button label="Back to Home" icon="pi pi-home" size="small" className="absolute top-0 left-0 m-4"
                    style={{boxShadow: "none"}} onClick={() => setShowHome(true)}/>
            <div className="flex flex-column align-items-center gap-3 p-4 card">
                {questionList}
                <Button label="Add question" icon="pi pi-plus-circle" size="small"
                        style={{boxShadow: "none"}} onClick={handleAddQuestion}/>
                <Button label="Create Survey!" icon="pi pi-send" size="small"
                        style={{boxShadow: "none"}} onClick={onSubmit}/>
            </div>
            </>
    );
};

export default CreateSurvey;