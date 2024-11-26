import SurveyCode from "../home/SurveyCode.jsx";

const Welcome = ({toast}) => {

    return (
        <>
            <div className="flex flex-column justify-content-center align-items-center gap-4 h-screen">
                <div className="flex justify-content-center">
                    <img src="monkeypoll-full-white.svg" alt="MonkeyPoll Logo"
                         style={{width: '50%', maxWidth: '1080px', height: 'auto'}}/>
                </div>
                <div className="flex pb-4">
                    <p className="text-2xl">Polls made simple.</p>
                </div>
                <div className="flex gap-2">
                    <SurveyCode toast={toast}/>
                </div>
            </div>
        </>
    )
}

export default Welcome;