import {useContext, useState} from 'react';
import {UserContext} from "../context/UserContext.jsx";
import {Button} from 'primereact/button';
import CreateSurvey from "../survey/CreateSurvey.jsx";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {getSurveyResponses} from "../api/surveyApi.js";
import {Dialog} from "primereact/dialog";
import SurveyCode from "./SurveyCode.jsx";

const Home = ({toast}) => {
    const [user] = useContext(UserContext);
    const [showCreateSurvey, setShowCreateSurvey] = useState(false);
    const [selectedSurveyResponses, setSelectedSurveyResponses] = useState(null);
    const [selectedSurvey, setSelectedSurvey] = useState(null);
    const [responseDialogVisible, setResponseDialogVisible] = useState(false);

    if (showCreateSurvey) {
        return <CreateSurvey toast={toast} setVisible={setShowCreateSurvey}/>
    }

    const getResponses = async (survey) => {

        const responsesStatus = await getSurveyResponses(survey["surveyId"]);
        if (responsesStatus.success) {
            return (responsesStatus.body);
        } else {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Response Retrieval Error',
                detail: responsesStatus.body.message,
            });
        }
    }
    const handleSelection = (e) => {
        setSelectedSurvey(e.value)
        getResponses(e.value).then(setSelectedSurveyResponses)
        setResponseDialogVisible(true)
    }

    const handleCloseDialog = () => {
        if (!responseDialogVisible) return;
        setResponseDialogVisible(false)
        setSelectedSurveyResponses(null)
        setSelectedSurvey(null)
    }

    return (
        <div className="home flex flex-column align-items-center justify-content-center h-screen">
            <Dialog header="Responses for selected survey" visible={responseDialogVisible} style={{width: '50vw'}}
                    onHide={handleCloseDialog}>
                <p className="m-0">
                    {<div>
                        <pre>{JSON.stringify(selectedSurveyResponses, null, 2)}</pre>
                    </div>}
                </p>
            </Dialog>

            {user ? (
                    <div>

                        <div className="flex justify-content-center">
                            <img src="monkeypoll-full-white.svg" alt="MonkeyPoll Logo"
                                 style={{width: '50%', maxWidth: '1080px', height: 'auto'}}/>
                        </div>
                        <div className="flex flex-column align-items-center gap-3 p-4 card">
                            <h2>Welcome, {user.firstName} {user.lastName}!</h2>
                            <SurveyCode large={false} />
                            <Button label="Create Survey" icon="pi pi-plus" size="small"
                                    style={{boxShadow: "none"}} onClick={() => setShowCreateSurvey(true)}/>
                            <DataTable value={user.surveys} selectionMode="single" selection={selectedSurvey}
                                       onSelectionChange={(e) => handleSelection(e)} tableStyle={{minWidth: '50rem'}}>
                                <Column field="surveyId" header="ID"></Column>
                                <Column field="description" header="Name"></Column>
                                <Column field="completions.length" header="Completions"></Column>
                            </DataTable>
                        </div>
                    </div>
                ) :
                null}
        </div>
    );
};

export default Home;
