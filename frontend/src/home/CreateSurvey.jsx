import { useState, useRef, useContext, useEffect } from 'react';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { UserContext } from "../context/UserContext.jsx";
import Home from '../home/Home.jsx';
import Question from "./Question.jsx";

const CreateSurvey = () => {
    const toast = useRef(null);
    const lastQuestionId = useRef(1)
    const [user] = useContext(UserContext);
    const [showHome, setShowHome] = useState(false);
    const [questionList, setQuestionList] = useState([<Question key={1} id={1}/>])

    const getUniqueId = () =>  {
        return ++lastQuestionId.current;
    }


    if (showHome) {
        return <Home />;
    }

    const onSubmit = async () => {
        //const createStatus = await createSurvey(questionList);
    };

    const handleAddQuestion = () => {
        const newId = getUniqueId()

        setQuestionList(prevState => {
            return [...prevState, <Question key={newId} id={newId}/>]
        })
    };

    return (
        <>
            <Button label="Back to Home" icon="pi pi-home" size="small" className="absolute top-0 left-0 m-4"
                    style={{boxShadow: "none"}} onClick={() => setShowHome(true)}/>
            <div className="flex flex-column align-items-center gap-3 p-4 card">
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