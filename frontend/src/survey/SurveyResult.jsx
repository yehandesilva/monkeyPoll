import {Button} from "primereact/button";
import TextQuestionResponse from "./TextQuestionResponse.jsx";

const SurveyResult = ({result, setResult}) => {
    console.log(result)
    return (
        <>
            <Button label="Back" icon="pi pi-arrow-circle-left" size="small" className="absolute top-0 left-0 m-4"
                    style={{boxShadow: "none"}} onClick={() => setResult(false)}/>
            <div className="flex flex-column align-items-center pt-8 pb-8">
                <div className="flex flex-column align-items-center">
                    <div className="text-4xl underline font-bold">{result.description}</div>
                </div>
                {
                    (result.responses).map((response, i) => {
                        if (response.questionType === 'TextQuestion') {
                            return <TextQuestionResponse response={response} index={i}/>
                        } else if (response.questionType === 'NumberQuestion') {
                            return null;
                        } else if (response.questionType === 'ChoiceQuestion') {
                            return null;
                        } else {
                            return null;
                        }
                    })
                }
            </div>
        </>
    )
}

export default SurveyResult;