import {Card} from "primereact/card";

const TextQuestionResponse = ({response, index}) => {

    return (
        <>
            <div key={index} className="w-6 py-4">
                <Card title={"Question " + (index + 1)}>
                    <h3>{response.question}</h3>
                    <ul>
                        {(response.responses.map((text, i) => {
                            return <li key={i}>{text}</li>
                        }))}
                    </ul>
                </Card>
            </div>

        </>
    )
}

export default TextQuestionResponse;