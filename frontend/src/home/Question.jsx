import { useState } from 'react';
import { InputText } from 'primereact/inputtext';
import {Card} from "primereact/card";
import {Dropdown} from "primereact/dropdown";
import {Slider} from "primereact/slider";
import PropTypes from "prop-types";
import NumberQuestionOptions from "./NumberQuestionOptions.jsx";

const Question = ({id}) => {
    const [prompt, setPrompt] = useState()
    const [type, setType] = useState()
    const [responseOptions, setResponseOptions] = useState()

    const questionTypes = [
        {label: "Text Question", value: "text"},
        {label: "Number Question", value: "number"},
        {label: "Choice Question", value: "choice"}
    ]

    const renderTypeSelection = () => {
        switch(type) {
            case "text":
                return <InputText disabled placeholder="No response options needed" className="w-full m-auto"/>
            case "number":
                return <NumberQuestionOptions/>
            case "choice":
                return <InputText placeholder="Option 1" />
            default:
                return null;


        }
    }

    return (
      <>
          <Card title={"Question " + id} className="w-6">
              <div className="formgrid grid">
                  <div className="field col-12 md:col-6">
                      <InputText type="text" placeholder="Enter question prompt" className="w-full"/>
                  </div>
                  <div className="field col-12 md:col-6">
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
    id: PropTypes.number.isRequired
};

export default Question
