const cp = require('child_process');
const fs = require('fs');

const pwd = __dirname;

function findDirectory({ current, name }) {
    var dirs = []
    const paths = fs.readdirSync(current);
  
    for (index in paths) {
        const path = `${current}/${paths[index]}`;
        const stat = fs.lstatSync(path);

        if (stat.isDirectory()) {
            dirs = dirs.concat(findDirectory({ current: path, name: name }));
        } else if (path.includes(name)) {
            console.log(path, name);
            dirs.push(current);
            break;
        }
    }

    return [...new Set(dirs)];
}

function updateAll({ name, format }) {
    const dirs = findDirectory({ current: pwd, name });
  
    // for (index in dirs) {
    //     const dir = dirs[index];
    //     console.log(dir);
    //     command = `${format}${dir}`

    //     function exec(command) {
    //         cp.exec(command, (e, stdout, stderr) => {
    //             if (e) {
    //                 console.log(`Error! Retrying "${command}"`);
    //                 exec(command);
    //             }
    //         })        
    //     }

    //     exec(command);
    // }
}

// updateAll({ name: 'Podfile', format: 'pod update --project-directory='});
updateAll({ name: '.git', format: 'git pull ' })