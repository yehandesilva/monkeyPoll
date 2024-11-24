import { useState, useRef, useContext, useEffect } from 'react';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { UserContext } from "../context/UserContext.jsx";
import Home from '../home/Home.jsx';
import Question from "./Question.jsx";
import {createSurvey} from "../api/surveyApi.js";
import NumberQuestionOptions from "./NumberQuestionOptions.jsx";
import ChoiceQuestionOptions from "./ChoiceQuestionOptions.jsx";

const CreateSurvey = () => {
    const toast = useRef(null);
    const lastQuestionId = useRef(1)
    const [user] = useContext(UserContext);
    const [showHome, setShowHome] = useState(false);
    const [surveyName, setSurveyName] = useState("")
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

        const creationData = formatCreationData()

        const creationStatus = await createSurvey(creationData);

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

    // Format the survey data into the form expected by the backend, returning the formatted Object.
    const formatCreationData = () => {
        let formattedQuestionList = []

        // iterate through each Question and convert each to proper format
        for (const [questionId, contents] of Object.entries(questionContents)) {
            let formattedQuestion = {
                question: contents["prompt"],
                type: contents["type"]
            }

            // Format the responseOptions of this question, based on the question type
            let responseOptions = contents["responseOptions"]
            if (contents["type"] === "NumberQuestion") {
                // For NumberQuestion types, need to add min and max value.
                formattedQuestion = {...formattedQuestion,
                    min: responseOptions["min"],
                    max: responseOptions["max"]}
            } else if (contents["type"] === "ChoiceQuestion") {
                // For ChoiceQuestion types, need to add each option in an "options" list
                let optionsList = []
                for (let option in responseOptions) {
                    if (responseOptions.hasOwnProperty(option)) {
                        optionsList = [...optionsList, {description: responseOptions[option]}]
                    }
                }
                formattedQuestion = {...formattedQuestion, options: optionsList}
            }
            // No options needed for TextQuestion types

            formattedQuestionList = [...formattedQuestionList, formattedQuestion]
        }

        return {
            description: surveyName,
            closed: false,
            questions: formattedQuestionList
        }
    }

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
                <div className="flex flex-column m-2 mt-6 mb-4" >
                    <label htmlFor="name" className="mb-1">Survey Name</label>
                    <InputText id="name" placeholder="Enter survey name" onChange={(e) =>
                        setSurveyName(e.target.value)} className="w-auto"/>
                </div>
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