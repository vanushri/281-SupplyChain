
token=$(curl -X POST \
		--header 'Content-Type: application/json' \
		--header 'Accept: application/json' \
		-d '{"username":"vanushri.rawat@sjsu.edu", "password":"admin"}' 'http://localhost:8080/api/auth/login' \
		| jq -r ".token")

#echo $token

for ((i=$1; i<=$2; i++))
do 
		 curl -X POST \
		--header 'Content-Type: application/json' \
		--header 'Accept: application/json' \
		--header "X-Authorization: Bearer $token" \
		--data '{"name":"TestSensor'$i'","type":"'$3'"}' 'http://localhost:8080/api/device'

	echo Sensor$i added !
done