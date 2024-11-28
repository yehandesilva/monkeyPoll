import {useEffect, useState} from 'react';
import { InputText } from 'primereact/inputtext';
import {Card} from "primereact/card";
import {Dropdown} from "primereact/dropdown";
import PropTypes from "prop-types";
import NumberQuestionOptions from "./NumberQuestionOptions.jsx";
import ChoiceQuestionOptions from "./ChoiceQuestionOptions.jsx";

const Question = ({id, setQuestionContents, prompt}) => {

    const [questionPrompt, setQuestionPrompt] = useState(prompt);
    const [type, setType] = useState()
    const [responseOptions, setResponseOptions] = useState({})

    const questionTypes = [
        {label: "Text Question", value: "TextQuestion"},
        {label: "Number Question", value: "NumberQuestion"},
        {label: "Choice Question", value: "ChoiceQuestion"}
    ]

    useEffect(() => {
        // need to clear response options when the type changes
        setResponseOptions({})
        updateQuestionContentsObj()

    }, [type]);

    useEffect(() => {
        updateQuestionContentsObj()
    }, [questionPrompt, responseOptions]);


    const updateQuestionContentsObj = () => {
        const contentsAsObject = {
            prompt: questionPrompt,
            type: type,
            responseOptions: responseOptions
        }

        setQuestionContents(prevState => {
            return {...prevState, [id]: contentsAsObject}
        })
    }

    const renderTypeSelection = () => {
        switch(type) {
            case "TextQuestion":
                return <InputText disabled placeholder="No response options needed" className="w-full m-auto"/>
            case "NumberQuestion":
                return <NumberQuestionOptions responseOptions={responseOptions} setResponseOptions={setResponseOptions}/>
            case "ChoiceQuestion":
                return <ChoiceQuestionOptions responseOptions={responseOptions} setResponseOptions={setResponseOptions}/>
            default:
                return null;

        }
    }

    return (
      <>
          <Card title={"Question " + id} className="w-6">
              <div className="formgrid grid">
                  <div className="field col-12 md:col-12">
                      <InputText value={questionPrompt} type="text" placeholder="Enter question prompt" onChange={(e) =>
                          setQuestionPrompt(e.target.value)} className="w-full"/>
                  </div>
                  <div className="field col-12 md:col-12">
                      <Dropdown value={type} onChange={(e) => setType(e.value)} options={questionTypes}
                                placeholder="Select a question type" className="w-full"/>
                  </div>
                  <div className="field col-12">
                      {renderTypeSelection()}
                  </div>
              </div>
          </Card>
      </>
)
}

Question.propTypes = {
    id: PropTypes.number.isRequired,
    setQuestionContents: PropTypes.func.isRequired
};

export default Question
