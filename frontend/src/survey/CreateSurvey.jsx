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
import question from "./Question.jsx";
import {getUser} from "../api/userApi.js";

const CreateSurvey = () => {
    const toast = useRef(null);
    const lastQuestionId = useRef(1)
    const [user, setUser] = useContext(UserContext);
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
        // Validate the form input before doing anything else, storing all error messages in a list
        // TODO: currently only the first error is display, but storing all errors anyways in case we need them in the future
        let validation_errors = [];

        // Ensure that the survey has a name
        if (!surveyName) {
            validation_errors.push("A survey name is required")
        }
        // Ensure that each field of each question is properly filled in
        for (const [questionId, contents] of Object.entries(questionContents)) {
            if (typeof contents["prompt"] === "undefined") {
                validation_errors.push('Prompt is required for question ' + questionId);
            }
            if (typeof contents["type"] === "undefined") {
                validation_errors.push('Question Type is required for question ' + questionId);

            } else {
                let responseOptions = contents["responseOptions"];
                if (contents["type"] === "NumberQuestion") {
                    if (typeof responseOptions["min"] === "undefined") {
                        validation_errors.push('Minimum value is required for question ' + questionId);
                    }
                    if (typeof responseOptions["max"] === "undefined") {
                        validation_errors.push('Maximum value is required for question ' + questionId);
                    }
                } else if (contents["type"] === "ChoiceQuestion") {
                    if (Object.entries(responseOptions).length === 0) {
                        validation_errors.push('Option(s) missing for question ' + questionId);
                    } else {
                        for (let option in responseOptions) {
                            if (responseOptions.hasOwnProperty(option)) {
                                if (typeof option === "undefined") {
                                    validation_errors.push('Option(s) missing for question ' + questionId);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (validation_errors.length > 0) {
            toast.current.show({
                severity: 'error',
                life: 7000,
                summary: 'Creation Input Error',
                detail: validation_errors[0],
            });
            return;
        }

        const creationData = formatCreationData()

        const creationStatus = await createSurvey(creationData);

        if (creationStatus.success) {
            toast.current.show({
                severity: 'success',
                life: 3000,
                summary: 'Survey successfully created!',
                detail: creationStatus.body.message,
            });
            const _user = await getUser();
            if (_user.success) {
                setUser(_user.body);
            }
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
        let formattedQuestionList = [];

        // iterate through each Question and convert each to proper format
        for (const [questionId, contents] of Object.entries(questionContents)) {
            let formattedQuestion = {
                question: contents["prompt"],
                type: contents["type"]
            }

            // Format the responseOptions of this question, based on the question type
            let responseOptions = contents["responseOptions"];
            if (contents["type"] === "NumberQuestion") {
                // For NumberQuestion types, need to add min and max value.
                formattedQuestion = {...formattedQuestion,
                    minValue: responseOptions["min"],
                    maxValue: responseOptions["max"]}
            } else if (contents["type"] === "ChoiceQuestion") {
                // For ChoiceQuestion types, need to add each option in an "options" list
                let optionsList = [];
                for (let option in responseOptions) {
                    if (responseOptions.hasOwnProperty(option)) {
                        optionsList = [...optionsList, {description: responseOptions[option]}];
                    }
                }
                formattedQuestion = {...formattedQuestion, options: optionsList};
            }
            // No options needed for TextQuestion types

            formattedQuestionList = [...formattedQuestionList, formattedQuestion];
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
            <Button label="Back" icon="pi pi-arrow-circle-left" size="small" className="absolute top-0 left-0 m-4"
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