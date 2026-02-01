import http from 'k6/http';

export const options = {
  stages: [
    { duration: '5s', target: 500 }, 
    { duration: '10s', target: 1000 }, 
    { duration: '5s', target: 0 }, 
  ],
};
export default function () {
  http.get('http://localhost:8080/api/products/1/price');
}