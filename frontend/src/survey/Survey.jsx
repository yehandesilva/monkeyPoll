import {useContext, useEffect, useRef, useState} from "react";
import { SurveyContext } from "../context/SurveyContext.jsx";
import { InputTextarea } from 'primereact/inputtextarea';
import {RadioButton} from "primereact/radiobutton";
import {Card} from "primereact/card";
import {InputNumber} from "primereact/inputnumber";
import {Toast} from "primereact/toast";
import {Button} from "primereact/button";
import {submitSurvey} from "../api/surveyApi.js";


/*
The Survey component models a Survey that the user can fill
out and submit.
 */
const Survey = () => {
    // Get reference to survey
    const [survey] = useContext(SurveyContext);
    const surveyQuestions = survey.questions;
    // Sort the array of questions based on questionId
    surveyQuestions.sort((a, b) => a.questionId - b.questionId);

    const toast = useRef(null);
    const [showMainPage, setShowMainPage] = useState(false);

    // Create useState hook to keep track of the user's response for each question
    // for each question using a Map -> [questionId, response] is the key-value pair
    const [responses, setResponses] = useState(new Map(surveyQuestions.map((question) => [question.questionId, null])));
    // Handle response change by updating the Map with the new value for the key
    const handleResponseChange = (e, questionId) => {
        const updatedResponse = e.target.value;
        console.log("Target value: " + updatedResponse);
        console.log("Question id: " + questionId);
        // Update the response value for the question and create new Map to force re-render
        setResponses(new Map(responses.set(questionId, updatedResponse)));
        console.log(responses.entries());
    }

    // Hook to direct user back to main MonkeyPoll page after survey submission
    useEffect(() => {
        setShowMainPage(!!showMainPage);
    }, [showMainPage]);


    // Handling clicking of submit button
    const surveySubmit = async () => {
        if (textValue && numValue && option) {
            const surveySubmissionStatus = await submitSurvey(survey);
            if (surveySubmissionStatus.success) {
                // Send the user back to the home
                setShowMainPage(surveySubmissionStatus.body);
            } else {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Survey Submission Error',
                    detail: surveySubmissionStatus.body.message,
                });
            }
        } else if (textValue === "") {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Survey Submission Error',
                detail: 'Text response is required',
            });
        } else if (!numValue) {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Survey Submission Error',
                detail: 'Number response is required',
            });
        } else if (!option) {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Survey Submission Error',
                detail: 'Choice response is required',
            });
        }
    }

    return (
        <>
            <Toast ref={toast} />
            <div className="flex flex-column align-items-center pt-8 pb-8">
                <div className="flex flex-column align-items-center">
                    <h1>{survey.description}</h1>
                    <h4>Survey code: {survey.surveyId}</h4>
                </div>
                <div className="flex flex-column align-items-center gap-5 pt-5">
                    {surveyQuestions.map(function(question) {
                        let questionId = question.questionId;
                        let questionStr = question.question;
                        // Question is a TextQuestion
                        if (question.type === "TextQuestion") {
                            return (
                                <div key={question.id} className="w-full">
                                    <Card title={"Question " + questionId}>
                                        <h3>{questionStr}</h3>
                                        <InputTextarea autoResize value={responses.get(questionId)}
                                                       onChange={(e) => handleResponseChange(e, questionId)} rows={10} cols={40}/>
                                    </Card>
                                </div>
                            );
                        }
                        // Question is a NumberQuestion
                        if (question.type === "NumberQuestion") {
                            return (
                                <div key={question.id} className="w-full">
                                    <Card title={"Question " + questionId}>
                                        <h3>{questionStr}</h3>
                                        <div className="flex-auto">
                                            <label htmlFor="minmax" className="block mb-2">Enter a number between {question.minValue} and {question.maxValue}</label>
                                            <InputNumber inputId="minmax" value={responses.get(questionId)}
                                                         onValueChange={(e) => handleResponseChange(e, questionId)} min={question.minValue}
                                                         max={question.maxValue}/>
                                        </div>
                                    </Card>
                                </div>
                            );
                        }
                        // Question is a ChoiceQuestion
                        if (question.type === "ChoiceQuestion") {
                            let choiceOptions = question.options;
                            return (
                                <div key={question.id} className="w-full">
                                    <Card title={"Question " + questionId}>
                                        <h3>{questionStr}</h3>
                                        <div className="flex flex-column gap-2">
                                            {choiceOptions.map(function(choiceOption) {
                                                // Create RadioButton for each option
                                                return (
                                                    <div key={choiceOption.id} className="flex align-items-center">
                                                        <RadioButton inputId={choiceOption.id} name={question} value={choiceOption.description} onChange={(e) => handleResponseChange(e, questionId)} checked={responses.get(questionId) === choiceOption.description} />
                                                        <label htmlFor={choiceOption.id} className="ml-2">{choiceOption.description}</label>
                                                    </div>
                                                );
                                            })}
                                        </div>
                                    </Card>
                                </div>
                            );
                        }
                    })}
                    <Button icon="pi pi-arrow-right" label="Submit" size="large" style={{ boxShadow: "none" }} onClick={() => surveySubmit()} />
                </div>
            </div>
        </>
    );
};
export default Survey;