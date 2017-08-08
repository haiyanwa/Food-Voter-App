function getStuff() {

    let person = {};

    return Promise.resolve("shay")
        .then(name => {
            person.name = name;
            console.log(name);
        })
        .then(() => {
            console.log(person);
        })
        .then(() => {
            console.log("start waiting");
            setTimeout(() => {
               console.log("waiting over!");
            }, 180000);
        })
}

getStuff();
