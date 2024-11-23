import {useContext, useState} from "react";
import { SurveyContext } from "../context/SurveyContext.jsx";
import { InputTextarea } from 'primereact/inputtextarea';
import {Knob} from "primereact/knob";
import {RadioButton} from "primereact/radiobutton";
import {Card} from "primereact/card";
import {InputNumber} from "primereact/inputnumber";


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

    let surveyQuestions = survey.questions;

    return (
        <div className="align-items-center pt-8">
            <div>
                <h1>{survey.description}</h1>
                <h4>Survey code: {survey.surveyId}</h4>
            </div>
            <div className="flex flex-column align-items-center gap-5 pt-5 h-full">
                {surveyQuestions.map(function(question) {
                    // Question is a TextQuestion
                    if (question.type === "TextQuestion") {
                        return (
                            <div key={question.id} className="w-full">
                                <Card title={"Question " + question.questionId} className="flex w-6">
                                    <h2>{question.question}</h2>
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
                                <Card title={"Question " + question.questionId} className="flex w-6">
                                    <h2>{question.question}</h2>
                                    <div className="flex-auto">
                                        <label htmlFor="minmax" className="font-bold block mb-2">Enter a number between {question.minValue} and {question.maxValue}</label>
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
                                <Card title={"Question " + question.questionId} className="flex w-6">
                                    <h2>{question.question}</h2>
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
    );
};
export default Survey;