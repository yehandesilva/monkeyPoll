import {Button} from "primereact/button";
import TextQuestionResponse from "./TextQuestionResponse.jsx";
import NumberQuestionResponse from "./NumberQuestionResponse.jsx";
import ChoiceQuestionResponse from "./ChoiceQuestionResponse.jsx";

const SurveyResult = ({result, setResult}) => {

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
                            return <NumberQuestionResponse response={response} index={i}/>;
                        } else if (response.questionType === 'ChoiceQuestion') {
                            return <ChoiceQuestionResponse response={response} index={i}/>;
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