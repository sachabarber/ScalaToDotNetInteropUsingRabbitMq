using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace Common
{
    public class Person
    {
        [JsonProperty(PropertyName="age")]
        public int Age { get; set; }

        [JsonProperty(PropertyName = "name")]
        public string Name { get; set; }

        public override string ToString()
        {
            return string.Format("Name : {0}, Age : {1}", Name, Age);
        }
    }
}
