import { useContext, useEffect, useRef, useState } from "react";
import { SurveyContext } from "../context/SurveyContext.jsx";
import { UserContext } from "../context/UserContext.jsx";
import { validateEmail } from "../modules/validation.js";
import { InputTextarea } from 'primereact/inputtextarea';
import { RadioButton } from "primereact/radiobutton";
import { Card } from "primereact/card";
import { InputNumber } from "primereact/inputnumber";
import { Toast } from "primereact/toast";
import { Button } from "primereact/button";
import { submitSurvey } from "../api/surveyApi.js";
import { InputText } from "primereact/inputtext";
import { FloatLabel } from "primereact/floatlabel";
import { Message } from "primereact/message";

/*
The Survey component models a Survey that the user can fill
out and submit.
 */
const Survey = ({toast}) => {
    // Get reference to survey and user context
    const [survey] = useContext(SurveyContext);
    const [user] = useContext(UserContext);

    // useState hook for setting/getting user's email address (if not logged in)
    const [email, setEmail] = useState('');

    // Sort the array of questions based on questionId
    const surveyQuestions = survey.questions;
    surveyQuestions.sort((a, b) => a.questionId - b.questionId);

    // Create useState hook to keep track of the user's response for each question
    // for each question using a Map -> [questionId, response] is the key-value pair
    const [responses, setResponses] = useState(new Map(surveyQuestions.map((question) => [question.questionId, undefined])));

    // Handle response change by updating the Map with the new value for the key
    const handleResponseChange = (e, questionId, questionType) => {
        let updatedResponse;
        if (questionType === "ChoiceQuestion") {
            updatedResponse = { choice: e.target.value, type: "choice" };
        } else {
            updatedResponse = e.target.value;
        }
        // Update the response value for the question and create new Map to force re-render
        setResponses(new Map(responses.set(questionId, updatedResponse)));
    };

    // Hook to update the email with the logged-in user's email
    useEffect(() => {
        if (user) {
            setEmail(user.email);
        }
    }, [user]);

    // Handling clicking of submit button
    const surveySubmit = async () => {
        // Set the user's email if they are logged in
        if (user) {
            setEmail(user.email);
        }
        // Check if email is not specified
        if (!email || typeof email == 'undefined') {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Survey Submission Error',
                detail: 'Email is required to fill out a survey',
            });
            return;
        } else if (!validateEmail(email)) {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Survey Submission Error',
                detail: 'Incorrect email format',
            });
            return;
        }

        // Traverse through each questionId-response pair and check its completion
        for (let [questionId, response] of responses) {
            // Incomplete response
            if (!response || typeof response == 'undefined') {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Survey Submission Error',
                    detail: 'Response is required for Question ' + questionId,
                });
                return;
            }
        }

        // Await result of submitting survey
        const surveySubmissionStatus = await submitSurvey(survey.surveyId, email, responses);
        if (surveySubmissionStatus.success) {
            // Show success message
            toast.current.show({
                severity: 'success',
                life: 3000,
                summary: 'Survey response submitted successfully',
                detail: 'Response submitted',
            });
        } else {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Survey Submission Error',
                detail: surveySubmissionStatus.body.message,
            });
        }
    };

    return (
        <>
            <div className="flex flex-column align-items-center pt-8 pb-8">
                <div className="flex flex-column align-items-center">
                    <h1>{survey.description}</h1>
                    <h3>Survey code: {survey.surveyId}</h3>
                </div>
                <div className="flex flex-column align-items-center gap-5 pt-5">
                    {user ?
                        <Message severity="info" text={"Submission email: " + user.email} />
                        : <div className="card flex justify-content-center">
                            <FloatLabel>
                                <InputText id="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                                <label htmlFor="email">Email address</label>
                            </FloatLabel>
                        </div>}
                    {surveyQuestions.map(function (question, questionIndex) {
                        let questionId = question.questionId;
                        let questionStr = question.question;
                        // Question is a TextQuestion
                        if (question.type === "TextQuestion") {
                            return (
                                <div key={questionId} className="w-full">
                                    <Card title={"Question " + (questionIndex + 1)}>
                                        <h3>{questionStr}</h3>
                                        <InputTextarea autoResize value={responses.get(questionId)}
                                                       onChange={(e) => handleResponseChange(e, questionId, question.type)} rows={10} cols={40} />
                                    </Card>
                                </div>
                            );
                        }
                        // Question is a NumberQuestion
                        if (question.type === "NumberQuestion") {
                            return (
                                <div key={questionId} className="w-full">
                                    <Card title={"Question " + (questionIndex + 1)}>
                                        <h3>{questionStr}</h3>
                                        <div className="flex-auto">
                                            <label htmlFor="minmax" className="block mb-2">Enter a number between {question.minValue} and {question.maxValue}</label>
                                            <InputNumber inputId="minmax" value={responses.get(questionId)}
                                                         onValueChange={(e) => handleResponseChange(e, questionId, question.type)} min={question.minValue}
                                                         max={question.maxValue} />
                                        </div>
                                    </Card>
                                </div>
                            );
                        }
                        // Question is a ChoiceQuestion
                        if (question.type === "ChoiceQuestion") {
                            let choiceOptions = question.options;
                            return (
                                <div key={questionId} className="w-full">
                                    <Card title={"Question " + (questionIndex + 1)}>
                                        <h3>{questionStr}</h3>
                                        <div className="flex flex-column gap-2">
                                            {choiceOptions.map(function (choiceOption) {
                                                // Create RadioButton for each option
                                                let choiceOptionId = choiceOption.choiceOptionId;
                                                return (
                                                    <div key={choiceOptionId} className="flex align-items-center">
                                                        <RadioButton
                                                            inputId={choiceOptionId}
                                                            name={question}
                                                            value={choiceOption.description}
                                                            onChange={(e) => handleResponseChange(e, questionId, question.type)}
                                                            checked={responses.get(questionId)?.choice === choiceOption.description}
                                                        />
                                                        <label htmlFor={choiceOptionId} className="ml-2">{choiceOption.description}</label>
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