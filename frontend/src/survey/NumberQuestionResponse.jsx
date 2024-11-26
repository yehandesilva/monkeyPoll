import {Card} from "primereact/card";
import {Chart} from 'primereact/chart';
import {useEffect, useState} from "react";

const NumberQuestionResponse = ({response, index}) => {

    const [chartData, setChartData] = useState({});
    const [chartOptions, setChartOptions] = useState({});

    useEffect(() => {
        const data = {
            labels: response.responses.map((res) => Object.keys(res)[0]),
            datasets: [
                {
                    label: "Amount",
                    data: response.responses.map((res) => Object.values(res)[0]),
                    backgroundColor: [
                        'rgba(255, 159, 64, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(153, 102, 255, 0.2)'
                    ],
                    borderColor: [
                        'rgb(255, 159, 64)',
                        'rgb(75, 192, 192)',
                        'rgb(54, 162, 235)',
                        'rgb(153, 102, 255)'
                    ],
                    borderWidth: 1
                }
            ]
        };
        const options = {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        };

        setChartData(data);
        setChartOptions(options);
    }, []);

    return (
        <>
            <div key={index} className="w-6 py-4">
                <Card title={"Question " + (index + 1)}>
                    <h3>{response.question}</h3>
                    <Chart type="bar" data={chartData} options={chartOptions}/>
                </Card>
            </div>
        </>
    )
}

export default NumberQuestionResponse;