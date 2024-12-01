import { useState, useRef, useContext } from 'react';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { UserContext } from "../context/UserContext.jsx";
import Question from "./Question.jsx";
import {createSurvey, getAiQuestions} from "../api/surveyApi.js";
import {getUser} from "../api/userApi.js";
import {Card} from "primereact/card";
import {Dialog} from "primereact/dialog";
import { Image } from 'primereact/image';
import {ProgressSpinner} from "primereact/progressspinner";

const CreateSurvey = ({toast, setVisible}) => {
    const lastQuestionId = useRef(0)
    const [user, setUser] = useContext(UserContext);
    const [surveyName, setSurveyName] = useState("")
    const [questionContents, setQuestionContents] = useState({})
    const [questionList, setQuestionList] = useState([])
    const [bobDialogVisible, setBobDialogVisible] = useState(false);
    const [generatedQuestionsVisible, setGeneratedQuestionsVisible] = useState(false);
    const [generatedQuestions, setGeneratedQuestions] = useState([]);
    const [loading, setLoading] = useState(false);

    const getUniqueId = () =>  {
        return ++lastQuestionId.current;
    }

    const onSubmit = async () => {
        // Validate the form input before doing anything else, storing all error messages in a list
        // currently only the first error is display, but storing all errors anyways in case we need them in the future
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
            setVisible(false);
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
            return [...prevState, <Question key={newId} id={newId} setQuestionContents={setQuestionContents} prompt=""/>]
        })
    };

    const generateQuestions = async () => {
        setLoading(true);
        const status = await getAiQuestions(surveyName);
        if (status.success) {
            const _generatedQuestions = [];
            Object.values(status.body)[0].forEach((question) => {
               _generatedQuestions.push(question);
            });
            setGeneratedQuestions(_generatedQuestions);
        } else {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Generation Error',
                detail: status.body.message,
            });
        }
        setLoading(false);
        setGeneratedQuestionsVisible(true);
        if (!bobDialogVisible) return; setBobDialogVisible(false);
    }

    const addGeneratedQuestion = (question) => {
        const newId = getUniqueId();
        setQuestionList(prevState => {
            return [...prevState, <Question key={newId} id={newId} setQuestionContents={setQuestionContents} prompt={question}/>]
        })
        const _generatedQuestions = [...generatedQuestions.filter((ques) => ques !== question)];
        setGeneratedQuestions(_generatedQuestions);
        if (_generatedQuestions.length === 0) {
            setGeneratedQuestionsVisible(false);
        }
    }

    return (
        <>
            <Dialog header="Uncle Bob Says:" visible={bobDialogVisible} position='bottom-right' style={{ width: '25vw' }} onHide={() => {if (!bobDialogVisible) return; setBobDialogVisible(false); }} draggable={false} resizable={false}>
                <div className="flex flex-row ">
                    <Image src="aiMonkey.png" alt="Image" width="100" />
                    <div className="ml-3">
                        <div>
                            {"Generate questions?"}
                        </div>
                        {
                            (loading) ?
                                <ProgressSpinner style={{width: '50px', height: '50px'}} strokeWidth="8" fill="var(--surface-ground)" animationDuration=".5s" />
                                :
                                <Button className="flex mt-2" label="Yes" size="small" icon="pi pi-check"  style={{boxShadow: "none"}} onClick={() => generateQuestions()}/>
                        }

                    </div>
                </div>
            </Dialog>
            <Dialog header="Generated Questions:" visible={generatedQuestionsVisible} style={{ width: '50vw' }} onHide={() => {if (!generatedQuestionsVisible) return; setGeneratedQuestionsVisible(false); }} draggable={false} resizable={false}>
                <div className="flex flex-column gap-2">
                    {
                        (generatedQuestions.map((ques, i) => {
                            return (
                                <div key={i} className="flex flex-row gap-2 align-items-center">
                                    <Button icon="pi pi-plus" outlined raised size="small" style={{boxShadow: "none"}} onClick={() => addGeneratedQuestion(ques)}/>
                                    {ques}
                                </div>
                            )
                        }))
                    }
                </div>
            </Dialog>

            <Button label="Back" icon="pi pi-arrow-circle-left" size="small" className="absolute top-0 left-0 m-4"
                    style={{boxShadow: "none"}} onClick={() => setVisible(false)}/>
            <div className="flex flex-column align-items-center gap-3 p-4 card">
                <div className="flex flex-column m-2 mt-6 mb-4 w-4" >
                    <Card title="Survey Name">
                        <InputText id="name" placeholder="Enter survey name" onChange={(e) =>
                            setSurveyName(e.target.value)} className="w-full" onBlur={() => (surveyName)? setBobDialogVisible(true) : null}/>
                    </Card>
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