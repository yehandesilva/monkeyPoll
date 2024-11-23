import {useContext, useRef, useState} from "react";
import { SurveyContext } from "../context/SurveyContext.jsx";
import { InputTextarea } from 'primereact/inputtextarea';
import {Knob} from "primereact/knob";
import {RadioButton} from "primereact/radiobutton";
import {Card} from "primereact/card";
import {InputNumber} from "primereact/inputnumber";
import {Toast} from "primereact/toast";


/*
The Survey component models a Survey that the user can fill
out and submit.
 */
const Survey = () => {
    // Get reference to survey
    const [survey] = useContext(SurveyContext);
    const [textValue, setTextValue] = useState('');
    const [numValue, setNumValue] = useState(0);
    const [option, setOption] = useState('');
    const toast = useRef(null);

    let surveyQuestions = survey.questions;
    // Sort the array of questions based on questionId
    surveyQuestions.sort((a, b) => a.questionId - b.questionId);

    return (
        <>
            <Toast ref={toast} />
            <div className="flex flex-column align-items-center pt-8">
                <div className="flex flex-column align-items-center">
                    <h1>{survey.description}</h1>
                    <h4>Survey code: {survey.surveyId}</h4>
                </div>
                <div className="flex flex-column align-items-center gap-5 pt-5">
                    {surveyQuestions.map(function(question) {
                        // Question is a TextQuestion
                        if (question.type === "TextQuestion") {
                            return (
                                <div key={question.id} className="w-full">
                                    <Card title={"Question " + question.questionId}>
                                        <h3>{question.question}</h3>
                                        <InputTextarea autoResize value={textValue}
                                                       onChange={(e) => setTextValue(e.target.value)} rows={10} cols={40}/>
                                    </Card>
                                </div>
                            );
                        }
                        // Question is a NumberQuestion
                        if (question.type === "NumberQuestion") {
                            return (
                                <div key={question.id} className="w-full">
                                    <Card title={"Question " + question.questionId}>
                                        <h3>{question.question}</h3>
                                        <div className="flex-auto">
                                            <label htmlFor="minmax" className="block mb-2">Enter a number between {question.minValue} and {question.maxValue}</label>
                                            <InputNumber inputId="minmax" value={numValue}
                                                         onValueChange={(e) => setNumValue(e.value)} min={question.minValue}
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
                                    <Card title={"Question " + question.questionId}>
                                        <h3>{question.question}</h3>
                                        <div className="flex flex-column gap-2">
                                            {choiceOptions.map(function(choiceOption) {
                                                // Create RadioButton for each option
                                                return (
                                                    <div key={choiceOption.id} className="flex align-items-center">
                                                        <RadioButton inputId={choiceOption.id} name={question.question} value={choiceOption.description} onChange={(e) => setOption(e.value)} checked={option === choiceOption.description} />
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
                </div>
            </div>
        </>
    );
};
export default Survey;